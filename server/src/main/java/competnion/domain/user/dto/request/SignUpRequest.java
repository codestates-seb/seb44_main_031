package competnion.domain.user.dto.request;

import competnion.domain.user.annotation.ValidPassword;
import competnion.domain.user.annotation.ValidUsername;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class SignUpRequest {
    @ValidUsername
    @NotBlank(message = "유저네임은 필수 항목입니다.")
    private String username;
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    private String email;
    @ValidPassword
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    @NotBlank
    private String address;
}
