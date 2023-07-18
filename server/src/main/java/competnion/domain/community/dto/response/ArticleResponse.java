package competnion.domain.community.dto.response;

import competnion.domain.community.dto.ArticleQueryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class ArticleResponse {
    private String title;
    private LocalDateTime startDate;

    public static ArticleResponse of(ArticleQueryDto articleQueryDto) {
        return new ArticleResponse(articleQueryDto.getTitle(), articleQueryDto.getStartDate());
    }
}
