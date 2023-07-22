package competnion.domain.community.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
public class ArticlePostRequest {
    @Length(min = 15, max = 100)
    @NotBlank
    private String title;
    @Length(min = 30, max = 250)
    @NotBlank
    private String body;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    @NotBlank
    private String location;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private ZonedDateTime startDate;
    @Pattern(regexp = "^(30|60|90|120|150|180)$")
    private String endDate;
    private Integer attendant;
    private List<Long> petIds;
}
