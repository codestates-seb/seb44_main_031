package competnion.global.security.userdetails;

import competnion.domain.user.entity.User;
import competnion.domain.user.repository.UserRepository;
import competnion.global.exception.BusinessLogicException;
import competnion.global.exception.ExceptionCode;
import competnion.global.util.CustomAuthorityUtils;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


import java.util.Collection;
import java.util.Optional;


/**
    SecurityContext에는 오직 Authentication 객체만 들어갈 수 있음
    Authentication에도 UserDetails(일반로그인), OAuthDetails(소셜로그인) 객체만 허용

    MemberDetailsService는 DB에서 사용자 정보를 가져와 UserDetails 객체 생성
 */


@Slf4j
@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final CustomAuthorityUtils authorityUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        /**
         Q : username만 받으면 credential 조회 시에 아이디만 확인하고 password 체크 없이 UserDetail 넘겨주는 거 아닌가요??
         A : AuthenticationProvider를 구현하는 DaoAuthenticationProvider에서 password 확인

         AuthenticationProvider(interface) ->
         AbstractUserDetailsAuthenticationProvider(abstract class) ->
         DaoAuthenticationProvider(implement class)
         */

        log.info("CustomUserDetailService - loadUserByUsername");

        log.info("크레덴셜 조회");
        log.info("로그인에서 받아온 ID(이메일)와 DB의 User 정보를 대조");

        Optional<User> optionalUser = userRepository.findByEmail(username);
        User findUser = optionalUser.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        log.info("DB에 유저 정보가 있으면 User -> 인증 주체인 PrincipalDetail 객체로 변환");
        return new UserDetail(findUser);
    }

    private final class UserDetail extends User implements UserDetails {
        UserDetail(User user) {
            idToDetails(user.getId());
            emailToDetails(user.getEmail());
            passwordToDetails(user.getPassword());
            rolesToDetails(user.getRoles());
        }

//        UserDetail(User user) {
//
//            UserDetail userDetail = (UserDetail) User.UserDetails()
//                                                    .id(user.getId())
//                                                    .email(user.getEmail())
//                                                    .password(user.getPassword())
//                                                    .roles(user.getRoles())
//                                                    .build();
//
//        }



        @Override //authoritUtils를 이용하여 db에서 조회한 회원의 이메일 정보를 바탕으로 Role 권한 정보 컬렉션 생성
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorityUtils.createAuthorities(this.getRoles());

        }

        @Override
        public String getUsername() {
            return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
