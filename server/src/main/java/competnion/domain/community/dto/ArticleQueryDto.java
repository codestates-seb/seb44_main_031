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
    private Long articleId;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime createdAt;

    @QueryProjection
    public ArticleQueryDto(String title, LocalDateTime startDate) {
        this.title = title;
        this.startDate = startDate;
    }

    @QueryProjection
    public ArticleQueryDto(Long articleId, String title, LocalDateTime startDate, LocalDateTime createdAt) {
        this.articleId = articleId;
        this.title = title;
        this.startDate = startDate;
        this.createdAt = createdAt;
    }
}
