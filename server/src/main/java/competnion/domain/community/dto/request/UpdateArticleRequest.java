package competnion.domain.community.dto.request;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
public class UpdateArticleRequest {
    private String title;
    private String body;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDate;
    @Pattern(regexp = "^(30|60|90|120|150|180)$")
    private String endDate;
}
