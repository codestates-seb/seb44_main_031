package competnion.domain.user.dto.request;

import competnion.global.common.annotation.ValidPassword;
import competnion.global.common.annotation.ValidUsername;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UpdateUsernameRequest {
    @ValidPassword
    @NotBlank
    private String password;
    @ValidUsername
    @NotBlank
    private String newNickname;
}
