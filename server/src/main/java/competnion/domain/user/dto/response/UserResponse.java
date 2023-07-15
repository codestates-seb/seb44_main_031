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
    private long id;
    private String username;
    private String address;
    private String imgUrl;
    private List<PetResponse> pets;

    public UserResponse(String username, String address, String imgUrl, List<PetResponse> pets) {
        this.username = username;
        this.address = address;
        this.imgUrl = imgUrl;
        this.pets = pets;
    }



    public static UserResponse of(User user, List<PetResponse> pets) {
        return new UserResponse(
                user.getUsername(),
                user.getAddress(),
                user.getImgUrl(),
                pets
        );
    }



    @Getter
    @AllArgsConstructor
    public static class InArticleResponse {

        private long id;
        private String username;
        private String imgUrl;
        private List<PetResponse.ForArticleResponse> pets;



    public static UserResponse.InArticleResponse getResponse(User user, List<PetResponse.ForArticleResponse> pets) {
            return new UserResponse.InArticleResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getImgUrl(),
                    pets
            );
        }

    }

}
