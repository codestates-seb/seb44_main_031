package competnion.domain.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserQueryDto {
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime createdAt;

    @QueryProjection
    public UserQueryDto(String title, LocalDateTime startDate, LocalDateTime createdAt) {
        this.title = title;
        this.startDate = startDate;
        this.createdAt = createdAt;
    }
}
