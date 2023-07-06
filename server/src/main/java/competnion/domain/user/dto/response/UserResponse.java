package competnion.domain.user.dto.response;

import competnion.domain.dog.Pet;
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
    private List<Pet> pets;

    public static UserResponse of(User user) {
        return new UserResponse(
                user.getUsername(),
                user.getAddress(),
                user.getImgUrl(),
                user.getPets()
        );
    }
}
