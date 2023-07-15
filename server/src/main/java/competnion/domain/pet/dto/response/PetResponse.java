package competnion.domain.pet.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import competnion.domain.pet.entity.Pet;
import competnion.domain.user.dto.response.UserResponse;
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


    public static PetResponse of(Pet pet) {
        return new PetResponse(
                pet.getId(),
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

    @Getter
    @AllArgsConstructor
    public static class ForArticleResponse {

       private String name;


       public static PetResponse.ForArticleResponse getSimplePetName(Pet pet) {
           return new ForArticleResponse(pet.getName());
       }

    }



}
