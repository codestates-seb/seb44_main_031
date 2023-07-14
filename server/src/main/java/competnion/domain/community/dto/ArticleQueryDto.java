package competnion.domain.community.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ArticleQueryDto {
    private String title;
    private LocalDateTime date;

    @QueryProjection
    public ArticleQueryDto(String title, LocalDateTime date) {
        this.title = title;
        this.date = date;
    }
}
