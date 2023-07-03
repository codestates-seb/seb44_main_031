package competnion.domain.user.dto.request;

import competnion.domain.user.entity.User;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class SignUpRequest {
    @Pattern(regexp = "^([a-zA-Z0-9가-힣]){2,10}$",
             message = "유저네임은 영문자, 숫자, 한글 중 하나 이상을 포함해야 하며, 2~10자 여야 합니다.")
    @NotBlank(message = "유저네임은 필수 항목입니다.")
    private String username;
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    private String email;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
             message = "비밀번호는 특수문자, 영문자, 숫자 포함 8~20자 여야 합니다.")
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;

    public User toUser() {
        return User.SignUp()
                .username(username)
                .email(email)
                .password(password)
                .build();
    }
}
