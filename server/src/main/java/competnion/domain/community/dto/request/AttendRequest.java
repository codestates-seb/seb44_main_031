package competnion.domain.community.dto.request;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class AttendRequest {
    private List<Long> petIds;
    private Long articleId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;
    @Pattern(regexp = "^(30|60|90|120|150|180)$")
    private String endDate;
    private int attendant;
}
