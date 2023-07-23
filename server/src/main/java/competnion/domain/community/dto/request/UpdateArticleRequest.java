package competnion.domain.community.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.time.ZonedDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
public class UpdateArticleRequest {
    private String title;
    private String body;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private ZonedDateTime startDate;
    @Pattern(regexp = "^(30|60|90|120|150|180)$")
    private String endDate;
}
