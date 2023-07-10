package competnion.domain.user.service;

import competnion.domain.user.dto.request.ResetPasswordRequest;
import competnion.domain.user.dto.request.SignUpRequest;
import competnion.domain.user.entity.User;
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
public class AuthService {

    private final UserService userService;
    private final CoordinateUtil coordinateUtil;
    private final EmailUtil emailUtil;
    private final RedisUtil redisUtil;

    @Transactional
    public void signUp(final SignUpRequest signUpRequest) {
        if (!checkDuplicatedUsername(signUpRequest.getUsername()))
            throw new BusinessException(INVALID_INPUT_VALUE);

        if (!checkDuplicatedEmail(signUpRequest.getEmail()))
            throw new BusinessException(INVALID_INPUT_VALUE);

        Point point = coordinateUtil.coordinateToPoint(signUpRequest.getLatitude(), signUpRequest.getLongitude());
        userService.saveUser(point, signUpRequest);
        redisUtil.deleteData(signUpRequest.getEmail());
    }

    @Transactional
    public void checkDuplicateEmailAndSendVerificationEmail(final String email) {
        if (!checkDuplicatedEmail(email)) throw new BusinessException(INVALID_INPUT_VALUE);
        sendEmail(email);
    }

    @Transactional
    public void checkValidateEmailAndSendEmail(final String email, final Long userId) {
        User user = userService.returnExistsUserByIdOrThrow(userId);
        if (!userService.checkEmailValidate(email, user))
            throw new BusinessException(INVALID_INPUT_VALUE);
        sendEmail(email);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userService.returnExistsUserByIdOrThrow(userId);
        userService.deleteUser(user);
    }

    public Boolean verifyEmailCode(final String code, final String email) {
        return code.equals(redisUtil.getData(email));
    }

    public Boolean checkDuplicatedUsername(final String username) {
        return userService.checkExistsUserByUsername(username);
    }

    public Boolean checkDuplicatedEmail(final String email) {
        return userService.checkExistsUserByEmail(email);
    }

    public void sendEmail(String email) {
        String code = emailUtil.generateRandomCode();
        redisUtil.setDataAndExpire(email, code, 600000L);
        emailUtil.sendVerificationEmail(email, code);
    }

    public void resetPassword(final ResetPasswordRequest resetPasswordRequest) {

    }
}
