package competnion.domain.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserQueryDto {
    private String title;
    private LocalDateTime meetingTime;
    private LocalDateTime createdAt;

    @QueryProjection
    public UserQueryDto(String title, LocalDateTime meetingTime, LocalDateTime createdAt) {
        this.title = title;
        this.meetingTime = meetingTime;
        this.createdAt = createdAt;
    }
}
