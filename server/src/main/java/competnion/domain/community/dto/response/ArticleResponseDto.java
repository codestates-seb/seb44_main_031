package competnion.domain.community.dto.response;

import competnion.domain.community.entity.Article;
import lombok.*;

import java.time.LocalDateTime;

@Getter
public class ArticleResponseDto {
        private Long articleId;
//        private Long userId;
//        private String name;
        private String title;
        private String body;
//        private LocalDateTime createdAt;
//        private LocalDateTime modifiedAt;

        public ArticleResponseDto(Article entity) {
                this.articleId = entity.getArticleId();
                this.title = entity.getTitle();
                this.body = entity.getBody();
//                this.userId = entity.getUser().getId();
//                this.createdAt = entity.getCreatedAt();
//                this.modifiedAt = entity.getModifiedAt();
        }
}
