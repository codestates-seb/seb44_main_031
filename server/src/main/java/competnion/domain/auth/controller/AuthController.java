package competnion.domain.auth.controller;

import competnion.global.common.annotation.UserContext;
import competnion.global.common.annotation.ValidUsername;
import competnion.domain.user.dto.request.SignUpRequest;
import competnion.domain.user.entity.User;
import competnion.global.response.Response;
import competnion.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;

@Validated
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController { // VerificationFilter 이후의 처리
    private final AuthService authService;

//    @PostMapping("/social-login/google")
//    public ResponseEntity doSocialLogin(@RequestBody LoginDto loginDto) {
//
//        // 로그인 자체는 필터에서 구현이 다 되었는데 추가적으로 던져줘야할 정보가 있을 경우에 작성
//
//
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/check-username")
    public Response<Void> checkUsername(@ValidUsername @RequestParam("username") final String username) {
        authService.checkDuplicatedUsername(username);
        return Response.success();
    }

    // 회원가입 이메일 중복체크 & 인증 코드 전송
    @GetMapping("/sign-up/send-verification-email")
    public Response<Void> checkDuplicateEmailAndSendVerificationEmail(@Email @RequestParam("email") final String email) {
        authService.sendVerificationEmail(email);
        return Response.success();
    }

    // 회원가입
    @PostMapping("/sign-up")
    public Response<Void> signUp(@Valid @RequestBody final SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return Response.success();
    }

    // 회원가입 이메일 인증
    @GetMapping("/verify-email")
    public Response<Void> verifyEmail(
            @RequestParam("code")         final String code,
            @Email @RequestParam("email") final String email
    ) {
        authService.verifyEmailCode(code, email);
        return Response.success();
    }

    // 회원탈퇴 이메일 코드 전송
    @GetMapping("/delete/send-verification-email")
    public Response<Void> checkValidateEmailAndSendEmail(
            @UserContext                  final User user,
            @Email @RequestParam("email") final String email
    ) {
        authService.checkValidateEmailAndSendEmail(user, email);
        return Response.success();
    }

    // 이메일 인증 후 유저 탈퇴
    @DeleteMapping("/delete")
    public Response<Void> deleteUser(@UserContext final User user) {
        authService.deleteUser(user);
        return Response.success();
    }

    @PostMapping("/reissue")
    public Response<Void> postReissue(HttpServletRequest request, HttpServletResponse response) {
        authService.reissue(request,response);
        return Response.success();
    }

    @DeleteMapping("/logout")
    public Response<Void> deleteLogout(HttpServletRequest request) {
        authService.logout(request);
        return Response.success();
    }
}
