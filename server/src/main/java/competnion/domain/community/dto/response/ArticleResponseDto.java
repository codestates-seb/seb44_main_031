package competnion.domain.community.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleResponseDto {
        private Long articleId;
        private Long userId;
        private String name;
        private String title;
        private String body;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
}
