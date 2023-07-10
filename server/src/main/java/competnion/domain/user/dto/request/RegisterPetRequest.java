package competnion.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class RegisterPetRequest {
    private String name;
    private LocalDateTime birth;
    private Boolean gender;
    private Boolean isNeutered;
//    private MultipartFile image;
    private String inoculated;
}
