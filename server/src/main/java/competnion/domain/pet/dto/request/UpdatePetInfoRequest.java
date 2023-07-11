package competnion.domain.pet.dto.request;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class UpdatePetInfoRequest {
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private Boolean gender;
    private Boolean neutralization;
    private String vaccine;
}
