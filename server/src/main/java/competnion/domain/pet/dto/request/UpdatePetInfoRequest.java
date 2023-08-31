package competnion.domain.pet.dto.request;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
public class UpdatePetInfoRequest {
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private Boolean gender;
    private Boolean isNeutralized;
    private Long breedId;
    @Pattern(regexp = "^(I|E)(N|S)(T|F)(J|P)$")
    private String mbti;
}
