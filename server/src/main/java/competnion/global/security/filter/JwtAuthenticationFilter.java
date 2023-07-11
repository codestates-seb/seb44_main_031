package competnion.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import competnion.domain.user.entity.User;
import competnion.global.auth.dto.LoginDto;
import competnion.global.auth.entity.RefreshToken;
import competnion.global.security.jwt.JwtTokenizer;
import competnion.global.auth.repository.RefreshTokenRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
    UsernamePasswordAuthenticationToken :
    Authentication을 구현한 AbstractAuthenticationToken의 하위 클래스
    인증 전, 인증 완료된 UsernamePasswordAuthenticationToken 객체 생성

    AuthenticationManager(인증 담당)
    구현체 : AuthenticationProvider(실무자), ProviderManager(실무자 관리)
    인증 절차가 완료되면 인증 완료된 UsernamePasswordAuthenticationToken 객체 생성, SecurityContext에 저장



 */

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenizer jwtTokenizer, RefreshTokenRepository refreshTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenizer = jwtTokenizer;
        this.refreshTokenRepository = refreshTokenRepository;
    }



    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        log.info("로그인 요청!");
        log.info("=================================================");

        log.info("AuthentificationFilter - attemptAuthentication");

        log.info("HttpServletRequest로 부터 LoginDto 객체 생성");
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

        log.info("LoginDto : UserEmail -> Principal, Password -> Credential");
        log.info("LoginDto 정보를 토대로 인증받을 UsernameAuthenticationToken 발급!");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),loginDto.getPassword());







        return authenticationManager.authenticate(authenticationToken);
        /**
         * authenticationManager -> authenticationProvider -> UserDetailService에서 Credential 조회
         *
         */


    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {


        log.info("AuthenticationFilter - successfulAuthentication");
        log.info("==============================================================");




        User user = (User) authResult.getPrincipal();

        String accessToken = jwtTokenizer.delegateAccessToken(user);
        String refreshToken = jwtTokenizer.delegateRefreshToken(user);


        log.info("refreshToken DB에 저장");
        RefreshToken refreshTokenInDb = RefreshToken.builder()
                                                    .userId(user.getId())
                                                    .encodedJws(refreshToken)
                                                    .build();

        refreshTokenRepository.save(refreshTokenInDb);


        log.info("인증 성공한 토큰 헤더에 Authorization 추가");
        response.setHeader("Authorization","Bearer " + accessToken);
        response.setHeader("Refresh", refreshToken);


        this.getSuccessHandler().onAuthenticationSuccess(request,response,authResult);

    }

}
