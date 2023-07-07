package competnion.domain.user.controller;

import competnion.domain.user.dto.request.ResetPasswordRequest;
import competnion.domain.user.dto.request.SignUpRequest;
import competnion.domain.user.service.AuthService;
import competnion.global.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // 회원가입 유저네임 중복체크
    @GetMapping("/check-username")
    public ResponseEntity<Response> checkUsername(@RequestParam("username") final String username) {
        authService.checkDuplicateUsername(username);

        return ResponseEntity.ok(Response.success());
    }

    // 이메일 중복체크 & 인증 코드 전송
    @GetMapping("/send-verification-email")
    public ResponseEntity<Response> checkDuplicateAndSendVerificationEmail(@RequestParam("email") final String email) {
        authService.checkDuplicateAndSendVerificationEmail(email);

        return ResponseEntity.ok(Response.success());
    }

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<Response> signUp(@Valid @RequestBody final SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);

        return ResponseEntity.ok(Response.success());
    }

    // 이메일 인증
    @GetMapping("/verify-email")
    public ResponseEntity<Response> verifyEmail(
            @RequestParam("code")  final String code,
            @RequestParam("email") final String email
    ) {
        authService.verifyEmailCode(code, email);

        return ResponseEntity.ok(Response.success());
    }

    // 비밃번호 재설정
    @PatchMapping("/reset-password")
    public ResponseEntity<Response> resetPassword(@Valid @RequestBody final ResetPasswordRequest resetPasswordRequest) {
        authService.resetPassword(resetPasswordRequest);

        return ResponseEntity.ok(Response.success());
    }
}
