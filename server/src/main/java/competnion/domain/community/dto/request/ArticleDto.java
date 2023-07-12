package competnion.domain.community.dto.request;

import competnion.domain.community.entity.Article;
import competnion.domain.user.entity.User;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.*;

public class ArticleDto {

    @Getter
    @Builder
    public static class ArticlePostDto {
        @Length(min = 15, max = 100)
        @NotBlank
        private String title;
        @Length(min = 30, max = 250)
        @NotBlank
        private String body;
        @NotNull
        private Double latitude;
        @NotNull
        private Double longitude;
        @NotBlank
        private String location;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate date;
        private Integer attendant;

//        @Builder
//        public ArticlePostDto(String title, String body, String location, LocalDate date, int attendant) {
//            this.title = title;
//            this.body = body;
//            this.location = location;
//            this.date = date;
//            this.attendant = attendant;
//        }

//        public Article toEntity() {
//            return Article.CreateArticle()
//                    .title(title)
//                    .body(body)
//                    .location(location)
//                    .date(date)
//                    .attendant(attendant)
//                    .build();
//        }
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

//        @Builder
//        public ArticlePatchDto(String title, String body, String location, LocalDateTime date,
//                               int attendant) {
//            this.title = title;
//            this.body = body;
//            this.location = location;
//            this.date = date;
//            this.attendant = attendant;
//        }
//        public Article toEntity() {
//            return Article.CreateArticle()
//                    .title(title)
//                    .body(body)
//                    .location(location)
//                    .date(date)
//                    .attendant(attendant)
//                    .build();
//        }
    }
}
