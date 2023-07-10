package competnion.global.security.controller;

import competnion.domain.user.annotation.ValidUsername;
import competnion.domain.user.dto.request.ResetPasswordRequest;
import competnion.domain.user.dto.request.SignUpRequest;
import competnion.global.security.service.AuthService;
import competnion.global.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;

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
    public Response<Boolean> checkUsername(@ValidUsername @RequestParam("username") final String username) {
        return Response.success(authService.checkDuplicatedUsername(username));
    }

    // 회원가입 이메일 중복체크 & 인증 코드 전송
    @GetMapping("/sign-up/send-verification-email")
    public Response<Void> checkDuplicateEmailAndSendVerificationEmail(@Email @RequestParam("email") final String email) {
        authService.checkDuplicateEmailAndSendVerificationEmail(email);
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
    public Response<Boolean> verifyEmail(
            @RequestParam("code")         final String code,
            @Email @RequestParam("email") final String email
    ) {
        return Response.success(authService.verifyEmailCode(code, email));
    }

    // 비밃번호 재설정
    @PatchMapping("/reset-password")
    public Response<Void> resetPassword(@Valid @RequestBody final ResetPasswordRequest resetPasswordRequest) {
        authService.resetPassword(resetPasswordRequest);
        return Response.success();
    }

    // 회원탈퇴 이메일 코드 전송
    @GetMapping("/delete/send-verification-email/{user-id}")
    public Response<Void> checkValidateEmailAndSendEmail(
            @Email @RequestParam("email")     final String email,
            @Positive @PathVariable("user-id") final Long userId
    ) {
        authService.checkValidateEmailAndSendEmail(email, userId);
        return Response.success();
    }

    // 이메일 인증 후 유저 탈퇴
    @DeleteMapping("/{user-id}")
    public Response<Void> deleteUser(
            @Positive @PathVariable("user-id") final Long userId
    ) {
        authService.deleteUser(userId);
        return Response.success();
    }


    @PostMapping("/reissue")
    public ResponseEntity postReissue(HttpServletRequest request, HttpServletResponse response) {

        authService.reissue(request,response);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/logout")
    public ResponseEntity deleteLogout(HttpServletRequest request) {

        authService.logout(request);
        return ResponseEntity.noContent().build();
    }





}
