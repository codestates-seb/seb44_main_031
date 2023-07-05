package competnion.domain.user.controller;

import competnion.domain.user.dto.request.ResetPasswordRequest;
import competnion.domain.user.dto.request.SignUpRequest;
import competnion.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // 회원가입 유저네임 중복체크
    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestParam("username") final String username) {
        return ResponseEntity.ok().build();
    }

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody final SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return ResponseEntity.ok().build();
    }

    // 이메일 중복체크 & 인증 코드 전송
    @GetMapping("/send-verification-email")
    public ResponseEntity<?> sendVerificationEmail(@RequestParam("email") final String email) {
        authService.sendVerificationEmail(email);
        return ResponseEntity.ok().build();
    }

    // 이메일 인증
    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(
            @RequestParam("code") final String code,
            @RequestParam("email") final String email
    ) {
        return ResponseEntity.ok().build();
    }

    // 비밃번호 재설정
    @PatchMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody final ResetPasswordRequest resetPasswordRequest) {
        authService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok().build();
    }
}
