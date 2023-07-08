package competnion.domain.user.service;

import competnion.domain.user.dto.request.ResetPasswordRequest;
import competnion.domain.user.dto.request.SignUpRequest;
import competnion.domain.user.entity.User;
import competnion.domain.user.repository.UserRepository;
import competnion.global.exception.BusinessException;
import competnion.global.util.CoordinateUtil;
import competnion.infra.mail.util.EmailUtil;
import competnion.infra.redis.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static competnion.global.exception.ErrorCode.INVALID_INPUT_VALUE;

/**
 * TODO : 예외처리 에러코드 추가 필요
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
    private final CoordinateUtil coordinateUtil;
    private final EmailUtil emailUtil;
    private final RedisUtil redisUtil;

    /**
     * 프론트에서 닉네임 중복체크와 이메일 인증을 안하고 회원가입을 시도하면 오류창 보여주기
     * 회원가입창에서 이메일 코드 인증 완료후 회원가입할때 백엔드에 isEmailAuthed 라는 true 값을 추가로 보내주기
     * 이메일 코드 인증을 안하고 회원가입버튼을 누르면 isEmailAuthed 값에 false
     */
    @Transactional
    @Override
    public void signUp(final SignUpRequest signUpRequest) {
        if (!checkDuplicatedEmail(signUpRequest.getEmail()))
            throw new BusinessException(INVALID_INPUT_VALUE);

        if (!checkDuplicatedUsername(signUpRequest.getUsername()))
            throw new BusinessException(INVALID_INPUT_VALUE);

        if (!signUpRequest.getIsEmailAuthed()) {
            throw new BusinessException(INVALID_INPUT_VALUE);
        }

        Point point = coordinateUtil.coordinateToPoint(signUpRequest.getLatitude(), signUpRequest.getLongitude());
        userRepository.save(buildUser(signUpRequest, point));
        redisUtil.deleteData(signUpRequest.getEmail());
    }

    @Transactional(readOnly = true)
    @Override
    public void checkDuplicateEmailAndSendVerificationEmail(final String email) {
        if (!checkDuplicatedEmail(email))
            throw new BusinessException(INVALID_INPUT_VALUE);

        String code = emailUtil.generateRandomCode();
        redisUtil.setDataAndExpire(email, code, 600000L);
        emailUtil.sendVerificationEmail(email, code);
    }

    @Override
    public Boolean verifyEmailCode(final String code, final String email) {
        return code.equals(redisUtil.getData(email));
    }

    @Override
    public Boolean checkDuplicatedUsername(final String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    @Override
    public Boolean checkDuplicatedEmail(final String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    @Override
    public void resetPassword(final ResetPasswordRequest resetPasswordRequest) {

    }

    private User buildUser(final SignUpRequest signUpRequest, Point point) {
        return User.SignUp()
                .email(signUpRequest.getEmail())
                .username(signUpRequest.getUsername())
//                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .password((signUpRequest.getPassword()))
                .address(signUpRequest.getAddress())
                .point(point)
                .build();
    }
}
