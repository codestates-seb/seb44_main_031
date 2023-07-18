package competnion.global.config;

import competnion.global.resolver.LoginUserArgumentResolver;
import competnion.global.security.filter.JwtAuthenticationFilter;
import competnion.global.security.filter.JwtVerificationFilter;
import competnion.global.security.handler.AuthFailureHandler;
import competnion.global.security.handler.AuthSuccessHandler;
import competnion.global.security.handler.UserAccessDeniedHandler;
import competnion.global.security.handler.UserAuthenticationEntryPoint;
import competnion.global.security.interceptor.JwtParseInterceptor;
import competnion.global.security.jwt.JwtTokenizer;
import competnion.global.auth.repository.RefreshTokenRepository;
import competnion.global.util.CustomAuthorityUtils;
import competnion.global.util.JwtUtils;
import competnion.infra.redis.util.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfiguration implements WebMvcConfigurer {
    private final LoginUserArgumentResolver loginUserArgumentResolver;
    private final CustomAuthorityUtils authorityUtils;
    private final RedisUtil redisUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public SecurityConfiguration(LoginUserArgumentResolver loginUserArgumentResolver, CustomAuthorityUtils authorityUtils, RedisUtil redisUtil, RefreshTokenRepository refreshTokenRepository) {
        this.loginUserArgumentResolver = loginUserArgumentResolver;
        this.authorityUtils = authorityUtils;
        this.redisUtil = redisUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Bean
    public JwtTokenizer jwtTokenizer() {
        return new JwtTokenizer();
    }

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils(jwtTokenizer());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .cors(withDefaults())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new UserAuthenticationEntryPoint())
                .accessDeniedHandler(new UserAccessDeniedHandler())
                .and()
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeHttpRequests( // MemberController 참고
                        auth -> auth

                                .antMatchers(HttpMethod.OPTIONS,"/**").permitAll() // Preflight request

                                .antMatchers(HttpMethod.DELETE,"/auth").hasRole("USER")
                                .antMatchers(HttpMethod.POST,"/auth/sign-up").permitAll()
                                .antMatchers(HttpMethod.GET,"/auth/sign-up/send-verification-email").permitAll()
                                .antMatchers(HttpMethod.GET,"/auth/verify-email").permitAll()
                                .antMatchers(HttpMethod.GET,"/auth/check-username").permitAll()
                                .antMatchers(HttpMethod.POST,"/auth/delete/send-verification-email").hasRole("USER")
                                .antMatchers(HttpMethod.POST,"/auth/reissue").permitAll()
                                .antMatchers(HttpMethod.DELETE, "/auth/logout").hasRole("USER")

                                .antMatchers(HttpMethod.PATCH,"/users/address/{user-id}").hasRole("USER")
                                .antMatchers(HttpMethod.PATCH,"/users/username/{user-id}").hasRole("USER")
                                .antMatchers(HttpMethod.PATCH,"/users/image/{user-id}").hasRole("USER")

                                .antMatchers(HttpMethod.POST,"/pets/register/").hasRole("USER")


                                .antMatchers(HttpMethod.POST,"/articles/**").hasRole("USER")
                                .antMatchers(HttpMethod.DELETE,"/articles/**").hasRole("USER")

                                .anyRequest().permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PATCH","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));



        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtParseInterceptor(jwtUtils()))
                .addPathPatterns(List.of("/articles/**", "/auth/**"));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer,HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter jwtAuthenticationFilter =
                    new JwtAuthenticationFilter(authenticationManager,jwtTokenizer(),refreshTokenRepository);

            jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new AuthSuccessHandler());
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new AuthFailureHandler());

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtUtils(),authorityUtils,redisUtil);

            builder
                    .addFilter(jwtAuthenticationFilter)
                    .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);
        }
    }
}