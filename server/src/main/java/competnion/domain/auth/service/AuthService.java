package competnion.domain.auth.service;

import competnion.domain.auth.event.AuthEmailEvent;
import competnion.domain.auth.repository.RefreshTokenRepository;
import competnion.domain.pet.entity.Pet;
import competnion.domain.user.dto.request.SignUpRequest;
import competnion.domain.user.entity.User;
import competnion.domain.user.repository.UserRepository;
import competnion.global.exception.BusinessLogicException;
import competnion.global.security.interceptor.JwtParseInterceptor;
import competnion.global.security.jwt.JwtTokenizer;
import competnion.global.util.CoordinateUtil;
import competnion.global.util.CustomAuthorityUtils;
import competnion.global.util.JwtUtils;
import competnion.infra.redis.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

import static competnion.global.exception.ExceptionCode.*;
import static java.lang.String.format;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtUtils jwtUtils;
    private final RedisUtil redisUtil;
    private final CoordinateUtil coordinateUtil;
    private final CustomAuthorityUtils authorityUtils;

    private final JwtTokenizer jwtTokenizer;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

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

    public void reissue(HttpServletRequest request, HttpServletResponse response) {

        // AccessToken Header & 만료시간 체크, 만료 response는 VerificationFilter에서 선처리
        // 클라이언트에서 요청할 때 access, refresh 둘다 있어야 한다.

        // 문제점 : 재발급 전의 토큰 유효기간 남아있으면 전 토큰으로도 가능
        // but, 클라이언트 측에서 남아있는 시간 계산해서 만료되었을 때에만 재발급 요청 보내주면 해결 가능하긴 함

        log.info("AuthService - reissue");
        log.info("=========================================================");

        if (request.getHeader("Authorization")==null) {
            throw new BusinessLogicException(ACCESS_TOKEN_NULL);
        }


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
            String newAccessToken = jwtTokenizer.delegateAccessToken(findUser);

            response.setHeader("Authorization","Bearer " + newAccessToken);
        }
        else {
            throw new BusinessLogicException(REFRESH_TOKEN_TAMPERED);
        }
    }

    public void signUp(final SignUpRequest request) {
        checkDuplicatedUsername(request.getUsername());
        checkDuplicatedEmail(request.getEmail());
//        verifyEmailCode(request.getCode(), request.getEmail());
        if (request.getAddress().isEmpty()) throw new BusinessLogicException(CAN_NOT_CLOSE);

        final Point point = coordinateUtil.coordinateToPoint(request.getLongitude(), request.getLatitude());
        final List<String> roles = authorityUtils.createRoles(request.getEmail());
        final String encode = passwordEncoder.encode(request.getPassword());

        saveUser(point, encode, roles, request);
        redisUtil.deleteData(request.getEmail());
    }

    @Transactional(readOnly = true)
    public void sendVerificationEmail(final String email) {
        boolean empty = redisUtil.getData(email).isEmpty();
        if (!empty) throw new BusinessLogicException(ALREADY_SEND);
        checkDuplicatedEmail(email);
        eventPublisher.publishEvent(new AuthEmailEvent(email));
    }

    public void verifyEmailCode(final String code, final String email) {
        String data = redisUtil.getData(email);
        if (!code.equals(data)) {
            throw new BusinessLogicException(INVALID_EMAIL_CODE, format("%s 이메일의 인증코드가 유효하지 않습니다❗", email));
        }
    }

    @Transactional(readOnly = true)
    public void checkValidateEmailAndSendEmail(final User user, final String email) {
        checkEmailValidate(user, email);
        eventPublisher.publishEvent(new AuthEmailEvent(email));
    }

    public void deleteUser(final User user) {
        List<Pet> pets = user.getPets();
        for (Pet pet : pets)
            if (pet.getArticle() != null)
                throw new BusinessLogicException(USER_ALREADY_ATTENDED, "참여중인 산책모임이 있어 탈퇴가 불가능합니다❗");

        userRepository.delete(user);
    }

    public void checkEmailValidate(final User user, final String email) {
        if (!user.getEmail().equals(email))
            throw new BusinessLogicException(INVALID_EMAIL, format("%s 이메일이 유효하지 않습니다❗", email));
    }

    public void checkDuplicatedUsername(final String nickname) {
        if (userRepository.findByNickname(nickname).isPresent())
            throw new BusinessLogicException(DUPLICATE_NICKNAME, format("%s 는(은) 중복된 닉네임 입니다❗", nickname));
    }

    public void checkDuplicatedEmail(final String email) {
        if (userRepository.findByEmail(email).isPresent())
            throw new BusinessLogicException(DUPLICATE_EMAIL, format("%s 중복된 이메일 입니다❗", email));
    }

    public void checkMatchPassword(final String password, final String encodePassword) {
        final boolean matches = passwordEncoder.matches(password, encodePassword);
        if (!matches) throw new BusinessLogicException(PASSWORD_NOT_MATCH, "패스워드가 틀립니다❗");
    }

    public void saveUser(
            final Point point,
            final String encode,
            final List<String> roles,
            final SignUpRequest request
    ) {
        userRepository.save(User.signUp()
                .email(request.getEmail())
                .nickname(request.getUsername())
                .password(encode)
                .address(request.getAddress())
                .point(point)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .imgUrl("https://mybucketforpetmily.s3.ap-northeast-2.amazonaws.com/dog.png")
                .roles(roles)
                .build());
    }
}
