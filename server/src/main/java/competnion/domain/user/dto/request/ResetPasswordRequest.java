package competnion.domain.user.dto.request;

import competnion.domain.user.annotation.ValidPassword;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class ResetPasswordRequest {
    @ValidPassword
    @NotBlank
    private String password;
    @ValidPassword
    @NotBlank
    private String newPassword;
    @ValidPassword
    @NotBlank
    private String newPasswordConfirm;
}
