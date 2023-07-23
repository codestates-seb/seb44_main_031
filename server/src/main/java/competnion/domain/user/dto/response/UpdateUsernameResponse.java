package competnion.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class UpdateUsernameResponse {
    private String username;

    public static UpdateUsernameResponse of(String username) {
        return new UpdateUsernameResponse(username);
    }
}
