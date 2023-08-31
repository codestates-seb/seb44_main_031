package competnion.domain.community.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ArticleQueryDto {
    private Long articleId;
    private String title;
    private ZonedDateTime startDate;
    private ZonedDateTime createdAt;

    @QueryProjection
    public ArticleQueryDto(String title, ZonedDateTime startDate) {
        this.title = title;
        this.startDate = startDate;
    }

    @QueryProjection
    public ArticleQueryDto(Long articleId, String title, ZonedDateTime startDate, ZonedDateTime createdAt) {
        this.articleId = articleId;
        this.title = title;
        this.startDate = startDate;
        this.createdAt = createdAt;
    }
}
