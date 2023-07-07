package competnion.domain.community.dto.request;

import competnion.domain.community.entity.Article;
import competnion.domain.user.entity.User;
import lombok.*;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.*;

public class ArticleDto {

    @Getter
    @Builder
    public static class ArticlePostDto {

        private String title;
        private String body;
        private String location;
        private LocalDateTime date;
        private int attendant;

        @Builder
        public ArticlePostDto(String title, String body, String location, LocalDateTime date, int attendant) {
            this.title = title;
            this.body = body;
            this.location = location;
            this.date = date;
            this.attendant = attendant;
        }

        public Article toEntity() {
            return Article.builder()
                    .title(title)
                    .body(body)
                    .location(location)
                    .date(date)
                    .attendant(attendant)
                    .build();
        }
    }
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticlePatchDto {
        private Long articleId;
        private String title;
        private String body;
        private String location;
        private LocalDateTime date;
        private int attendant;

        @Builder
        public ArticlePatchDto(String title, String body, String location, LocalDateTime date,
                               int attendant) {
            this.title = title;
            this.body = body;
            this.location = location;
            this.date = date;
            this.attendant = attendant;
        }
        public Article toEntity() {
            return Article.builder()
                    .title(title)
                    .body(body)
                    .location(location)
                    .date(date)
                    .attendant(attendant)
                    .build();
        }
    }
}
