package competnion.domain.pet.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import competnion.domain.pet.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class PetResponse {
    private Long id;
    private String name;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private Boolean gender;
    private Boolean neutralization;
    private String imgUrl;
    private String vaccine;

    private PetResponse(Long id, String name, String imgUrl) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
    }

    private PetResponse(String name, LocalDate birth, Boolean gender, Boolean neutralization, String imgUrl, String vaccine) {
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.neutralization = neutralization;
        this.imgUrl = imgUrl;
        this.vaccine = vaccine;
    }

    public static PetResponse of(Pet pet) {
        return new PetResponse(
                pet.getName(),
                pet.getBirth(),
                pet.getGender(),
                pet.getNeutralization(),
                pet.getImgUrl(),
                pet.getVaccine()
        );
    }

    public static PetResponse simple(Pet pet) {
        return new PetResponse(
                pet.getId(),
                pet.getName(),
                pet.getImgUrl()
        );
    }
}
