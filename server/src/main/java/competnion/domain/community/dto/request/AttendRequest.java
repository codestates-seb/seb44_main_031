package competnion.domain.community.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
public class AttendRequest {
    private List<Long> petIds;
    private Long articleId;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private ZonedDateTime startDate;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private ZonedDateTime endDate;
    private int attendant;
}
