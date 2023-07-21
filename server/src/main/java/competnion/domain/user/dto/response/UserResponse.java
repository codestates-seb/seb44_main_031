package competnion.domain.user.dto.response;

import com.querydsl.core.annotations.QueryProjection;
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
    private Double latitude;
    private Double longitude;
    private List<PetResponse> pets;

    private UserResponse(String username, String address, String imgUrl, Double latitude, Double longitude, List<PetResponse> pets) {
        this.username = username;
        this.address = address;
        this.imgUrl = imgUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pets = pets;
    }

    public static UserResponse of(User user, List<PetResponse> pets) {
        return new UserResponse(
                user.getNickname(),
                user.getAddress(),
                user.getImgUrl(),
                user.getLatitude(),
                user.getLongitude(),
                pets
        );
    }



    @Getter
    @AllArgsConstructor
    public static class InArticleResponse {

        private long userId;
        private String nickname;
        private String imgUrl;
        private List<PetResponse.ForArticleResponse> pets;



        public static UserResponse.InArticleResponse getResponse(User user, List<PetResponse.ForArticleResponse> pets) {
            return new UserResponse.InArticleResponse(
                    user.getId(),
                    user.getNickname(),
                    user.getImgUrl(),
                    pets
            );
        }

    }

    @Getter
    @AllArgsConstructor
    public static class OfMultiArticleResponse {
        private String address;
        private Double latitude;
        private Double longitude;

        public static  UserResponse.OfMultiArticleResponse getResponse(User user) {
            return new OfMultiArticleResponse(
                    user.getAddress(),
                    user.getLatitude(),
                    user.getLongitude()
            );
        }
    }
}