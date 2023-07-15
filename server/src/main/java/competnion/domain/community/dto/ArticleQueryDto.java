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
    private LocalDateTime date;
    private LocalDateTime createdAt;

    @QueryProjection
    public ArticleQueryDto(String title, LocalDateTime date) {
        this.title = title;
        this.date = date;
    }

    @QueryProjection
    public ArticleQueryDto(Long articleId, String title, LocalDateTime date, LocalDateTime createdAt) {
        this.articleId = articleId;
        this.title = title;
        this.date = date;
        this.createdAt = createdAt;
    }
}
