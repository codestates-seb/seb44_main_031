package competnion.global.security.service;

import competnion.domain.user.dto.request.ResetPasswordRequest;
import competnion.domain.user.dto.request.SignUpRequest;
import competnion.domain.user.entity.User;
import competnion.domain.user.repository.UserRepository;
import competnion.domain.user.service.UserService;
import competnion.global.exception.BusinessLogicException;
import competnion.global.exception.ExceptionCode;
import competnion.global.security.interceptor.JwtParseInterceptor;
import competnion.global.security.jwt.JwtTokenizer;
import competnion.global.security.repository.RefreshTokenRepository;
import competnion.global.util.CoordinateUtil;
import competnion.global.util.CustomAuthorityUtils;
import competnion.global.util.JwtUtils;
import competnion.infra.mail.util.EmailUtil;
import competnion.infra.redis.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static competnion.global.exception.ExceptionCode.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenizer jwtTokenizer;
    private final UserService userService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CoordinateUtil coordinateUtil;
    private final CustomAuthorityUtils authorityUtils;
    private final PasswordEncoder passwordEncoder;
    private final EmailUtil emailUtil;
    private final JwtUtils jwtUtils;
    private final RedisUtil redisUtil;

    @Transactional
    public void logout(HttpServletRequest request) {
        log.info("AuthService - logout");
        log.info("=======================================");

        // 검증은 VerificationFilter에서 선처리

        log.info("DB에서 RefreshToken을 삭제하여 재발급 불가하게 만들기");
        long userIdInToken = JwtParseInterceptor.getAuthenticatedUserId();
        refreshTokenRepository.deleteTokenByUserId(userIdInToken);


        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");
        long remainingTokenExpirationTime = jwtUtils.getRemainingTokenExpiration(request);

        log.info("Redis에 request로 온 AccessToken BlackList에 넣기 (해당 토큰으로 오는 모든 요청 거부)");
        redisUtil.setBlackList(accessToken,"logout",remainingTokenExpirationTime);
        log.info("Blacklist 추가 완료");
    }

    @Transactional
    public void reissue(HttpServletRequest request, HttpServletResponse response) {

        // AccessToken Header & 만료시간 체크, 만료 response는 VerificationFilter에서 선처리
        // 클라이언트에서 요청할 때 access, refresh 둘다 있어야 한다.

        // 문제점 : 재발급 전의 토큰 유효기간 남아있으면 전 토큰으로도 가능
        // but, 클라이언트 측에서 남아있는 시간 계산해서 만료되었을 때에만 재발급 요청 보내주면 해결 가능하긴 함

        log.info("AuthService - reissue");
        log.info("=========================================================");

        if(request.getHeader("Refresh")==null) {
            throw new BusinessLogicException(REFRESH_TOKEN_NULL);
        }

        log.info("request AccessToken에서 memberId 추출 ");
        long userIdInToken = JwtParseInterceptor.getAuthenticatedUserId();

        String refreshToken = request.getHeader("Refresh");

        String refreshTokenInDb = refreshTokenRepository.findTokenByUserId(userIdInToken).getEncodedJws();

        log.info("RefreshToken 대조 : request vs DB");
        if(refreshToken.equals(refreshTokenInDb)) {

            log.info("DB에서 존재 확인 - AccessToken 재발급 절차 진행");
            log.info("=========================================================");


            log.info("request에서 추출한 memberId로 member 조회");
            Optional<User> user = userRepository.findById(userIdInToken);
            User findUser = user.orElseThrow(() ->new BusinessLogicException(USER_NOT_FOUND));


            log.info("추출한 member정보로 AccessToken 생성");
            Map<String,Object> claims = new HashMap<>();
            claims.put("memberId",findUser.getId());
            claims.put("nickname",findUser.getUsername());
            claims.put("roles",findUser.getRoles());
            String subject = findUser.getEmail();
            Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
            String encodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

            String newAccessToken = jwtTokenizer.generateAccessToken(claims,subject,expiration,encodedSecretKey);

            response.setHeader("Authorization","Bearer " + newAccessToken);
        }
        else {
            throw new BusinessLogicException(REFRESH_TOKEN_TAMPERED);
        }
    }

    @Transactional
    public void signUp(final SignUpRequest signUpRequest) {
        if (!checkDuplicatedUsername(signUpRequest.getUsername()))
            throw new BusinessLogicException(INVALID_INPUT_VALUE);

        if (!checkDuplicatedEmail(signUpRequest.getEmail()))
            throw new BusinessLogicException(INVALID_INPUT_VALUE);

        Point point = coordinateUtil.coordinateToPoint(signUpRequest.getLatitude(), signUpRequest.getLongitude());
        List<String> roles = authorityUtils.createRoles(signUpRequest.getEmail());
        String encode = passwordEncoder.encode(signUpRequest.getPassword());

        userService.saveUser(point, signUpRequest, encode, roles);
        redisUtil.deleteData(signUpRequest.getEmail());
    }

    @Transactional
    public void checkDuplicateEmailAndSendVerificationEmail(final String email) {
        if (!checkDuplicatedEmail(email)) throw new BusinessLogicException(INVALID_INPUT_VALUE);
        sendEmail(email);
    }

    @Transactional
    public void checkValidateEmailAndSendEmail(final String email, final Long userId) {
        User user = userService.returnExistsUserByIdOrThrow(userId);
        if (!userService.checkEmailValidate(email, user))
            throw new BusinessLogicException(INVALID_INPUT_VALUE);
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
