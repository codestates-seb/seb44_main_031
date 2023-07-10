package competnion.domain.pet.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import competnion.domain.pet.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class PetResponse {
    private String name;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private Boolean gender;
    private Boolean neutralization;
    private String imgUrl;
    private String vaccine;
    private Boolean isMain;

    public static PetResponse of(Pet pet) {
        return new PetResponse(
                pet.getName(),
                pet.getBirth(),
                pet.getGender(),
                pet.getNeutralization(),
                pet.getImgUrl(),
                pet.getVaccine(),
                pet.getIsSelected()
        );
    }
}