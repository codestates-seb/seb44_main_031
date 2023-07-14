package competnion.domain.community.dto.response;

import competnion.domain.community.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class ArticleResponse {
    private String title;

    public static ArticleResponse of(Article article) {
        return new ArticleResponse(article.getTitle());
    }
}
