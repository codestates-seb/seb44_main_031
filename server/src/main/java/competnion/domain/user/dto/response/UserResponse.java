package competnion.domain.user.dto.response;

import competnion.domain.pet.dto.response.PetResponse;
import competnion.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static lombok.AccessLevel.*;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class UserResponse {
    private String username;
    private String address;
    private String imgUrl;
    private List<PetResponse> pets;

    public static UserResponse of(User user, List<PetResponse> pets) {
        return new UserResponse(
                user.getUsername(),
                user.getAddress(),
                user.getImgUrl(),
                pets
        );
    }
}
