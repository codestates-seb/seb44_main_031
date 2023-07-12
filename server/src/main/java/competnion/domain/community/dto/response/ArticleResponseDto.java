package competnion.domain.community.dto.response;

import competnion.domain.community.entity.Article;
import competnion.domain.user.dto.response.UserResponse;
import competnion.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class ArticleResponseDto {
        private Long articleId;
        private String username;
        private String title;
        private String body;
        private List<String> imageUrls;
//        private List<CommentDto.Response> comments;

        public static ArticleResponseDto of(Article article, List<String> imageUrls) {
                return new ArticleResponseDto(
                        article.getId(),
                        article.getUser().getUsername(),
                        article.getTitle(),
                        article.getBody(),
                        imageUrls
                );
        }
}
