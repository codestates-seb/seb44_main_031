package competnion.domain.user.service;

import competnion.domain.user.dto.request.ResetPasswordRequest;
import competnion.domain.user.dto.request.SignUpRequest;
import competnion.domain.user.entity.User;
import competnion.domain.user.repository.UserRepository;
import competnion.global.exception.BusinessException;
import competnion.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static competnion.global.exception.ErrorCode.INVALID_INPUT_VALUE;

/**
 * TODO : 예외처리 에러코드 추가 필요
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RedisUtil redisUtil;

    private Boolean isCheckedUsername = false;
    private Boolean isAuthedEmail = false;

    @Override
    public void signUp(final SignUpRequest signUpRequest) {
        if (!isAuthedEmail)     throw new BusinessException(INVALID_INPUT_VALUE);
        if (!isCheckedUsername) throw new BusinessException(INVALID_INPUT_VALUE);

        userRepository.save(buildUser(signUpRequest));
    }

    @Override
    public void sendVerificationEmail(final String email) {
        if (userRepository.findByEmail(email).isPresent())
            throw new BusinessException(INVALID_INPUT_VALUE);

        String code = emailService.generateRandomCode();
        redisUtil.setDataAndExpire(email, code, 600000L);

        emailService.sendVerificationEmail(email, code);
    }

    @Override
    public void verifyEmail(final String code, final String email) {
        if (code.equals(redisUtil.getData(email)))
            isAuthedEmail = true;
        else
            throw new BusinessException(INVALID_INPUT_VALUE);
    }

    @Override
    public void isDuplicateUsername(final String username) {
        if (userRepository.findByUsername(username).isEmpty())
            isCheckedUsername = true;
        else
            throw new BusinessException(INVALID_INPUT_VALUE);
    }

    @Override
    public void resetPassword(final ResetPasswordRequest resetPasswordRequest) {

    }

    private User buildUser(final SignUpRequest signUpRequest) {
        return User.SignUp()
                .email(signUpRequest.getEmail())
                .username(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .build();
    }


//    @Override
//    public Point coordinateToPoint(Double latitude, Double longitude) throws ParseException {
//        if (latitude != null && longitude != null)
//            return (Point) new WKTReader().read(String.format("POINT(%s %s)", latitude, longitude));
//        else
//            throw new BusinessException(INVALID_INPUT_VALUE);
//    }
}
