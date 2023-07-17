package competnion.global.resolver;

import competnion.domain.user.annotation.UserContext;
import competnion.domain.user.repository.UserRepository;
import competnion.global.exception.BusinessLogicException;
import competnion.global.security.userdetails.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static competnion.global.exception.ExceptionCode.USER_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserContext.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String nickname = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByNickname(nickname).orElseThrow(() -> new BusinessLogicException(USER_NOT_FOUND));
    }
}
