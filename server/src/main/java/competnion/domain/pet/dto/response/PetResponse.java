package competnion.domain.pet.dto.response;

import competnion.domain.pet.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class PetResponse {
    private String name;
    private LocalDateTime birth;
    private Boolean gender;
    private Boolean isNeutered;
    private String imgUrl;
    private String inoculated;
    private Boolean isMain;

    public static PetResponse of(Pet pet) {
        return new PetResponse(
                pet.getName(),
                pet.getBirth(),
                pet.getGender(),
                pet.getIsNeutered(),
                pet.getImgUrl(),
                pet.getInoculated(),
                pet.getIsMain()
        );
    }
}
