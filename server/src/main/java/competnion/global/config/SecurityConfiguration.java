package competnion.global.config;

import competnion.global.security.filter.JwtAuthenticationFilter;
import competnion.global.security.filter.JwtVerificationFilter;
import competnion.global.security.handler.AuthFailureHandler;
import competnion.global.security.handler.AuthSuccessHandler;
import competnion.global.security.handler.UserAccessDeniedHandler;
import competnion.global.security.handler.UserAuthenticationEntryPoint;
import competnion.global.security.interceptor.JwtParseInterceptor;
import competnion.global.security.jwt.JwtTokenizer;
import competnion.global.security.repository.RefreshTokenRepository;
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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity(debug = false)
public class SecurityConfiguration implements WebMvcConfigurer {
    private final CustomAuthorityUtils authorityUtils;

    private final RedisUtil redisUtil;

    private final RefreshTokenRepository refreshTokenRepository;

    public SecurityConfiguration(CustomAuthorityUtils authorityUtils, RedisUtil redisUtil, RefreshTokenRepository refreshTokenRepository) {
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

                                .antMatchers(HttpMethod.POST,"/auth/reissue").permitAll()
                                .antMatchers(HttpMethod.DELETE, "/auth/logout").hasRole("USER")

                                .antMatchers(HttpMethod.POST,"/members").permitAll() // 회원가입
                                .antMatchers(HttpMethod.PATCH, "/members/**").hasRole("USER") //회원 수정
                                .antMatchers(HttpMethod.GET, "/members/**").permitAll()
                                .antMatchers(HttpMethod.GET, "/members").hasRole("USER") // User tab
                                .antMatchers(HttpMethod.GET,"/members/users/**").hasRole("USER") // MYPAGE
                                .antMatchers(HttpMethod.DELETE, "/members/**").hasRole("USER")


                                .antMatchers(HttpMethod.POST, "/*/questions").hasRole("USER")
                                .antMatchers(HttpMethod.PATCH, "/questions/**").hasRole("USER")
                                .antMatchers(HttpMethod.GET, "/questions/**").permitAll()
                                .antMatchers(HttpMethod.GET, "/questions/**").permitAll()
                                .antMatchers(HttpMethod.DELETE, "/questions/**").hasRole("USER")

                                .antMatchers(HttpMethod.POST, "/answers").hasRole("USER")
                                .antMatchers(HttpMethod.PATCH, "/answers/**").hasRole("USER")
                                .antMatchers(HttpMethod.GET, "/answers/**").hasRole("USER")
                                .antMatchers(HttpMethod.GET, "/answers").permitAll()
                                .antMatchers(HttpMethod.DELETE, "/answers/**").hasRole("USER")

                                .antMatchers(HttpMethod.POST, "/comments").hasRole("USER")
                                .antMatchers(HttpMethod.PATCH, "/comments/**").hasRole("USER")
                                .antMatchers(HttpMethod.GET, "/comments/**").hasRole("USER")
                                .antMatchers(HttpMethod.GET, "/comments").hasRole("USER")
                                .antMatchers(HttpMethod.DELETE, "/comments/**").hasRole("USER")

                                .antMatchers(HttpMethod.POST, "/votes/**").hasRole("USER")
                                .antMatchers(HttpMethod.DELETE, "/votes/**").hasRole("USER")


                                .anyRequest().permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PATCH","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));



        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtParseInterceptor(jwtUtils()))
                .addPathPatterns(List.of("/*/questions","/questions/**","/answers/**","/auth/**","/votes/**"));

    }


    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer,HttpSecurity> {

        @Override
        public void configure(HttpSecurity builder) throws Exception {

            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter jwtAuthenticationFilter =
                    new JwtAuthenticationFilter(authenticationManager,jwtTokenizer(),refreshTokenRepository);

            jwtAuthenticationFilter.setFilterProcessesUrl("/members/login");
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new AuthSuccessHandler());
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new AuthFailureHandler());

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtUtils(),authorityUtils,redisUtil);

            builder
                    .addFilter(jwtAuthenticationFilter)
                    .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);

        }
    }
}
