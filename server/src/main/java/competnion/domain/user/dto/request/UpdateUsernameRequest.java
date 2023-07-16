package competnion.domain.user.dto.request;

import competnion.domain.user.annotation.ValidPassword;
import competnion.domain.user.annotation.ValidUsername;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UpdateUsernameRequest {
    @ValidPassword
    @NotBlank
    private String password;
    @ValidUsername
    private String newUsername;
}
