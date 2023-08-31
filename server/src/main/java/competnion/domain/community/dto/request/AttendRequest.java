package competnion.domain.community.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
public class AttendRequest {
    private List<Long> petIds;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private ZonedDateTime startDate;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private ZonedDateTime endDate;
    private int attendCapacity;
}
