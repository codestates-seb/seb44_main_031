package competnion.domain.community.dto.response;

import competnion.domain.pet.dto.response.PetResponse;
import competnion.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class WriterResponse {
    private Double latitude;
    private Double longitude;
    private List<PetResponse> pets;

    public static WriterResponse of(final User user, List<PetResponse> pets) {
        return new WriterResponse(
                user.getLatitude(),
                user.getLongitude(),
                pets
        );
    }
}
