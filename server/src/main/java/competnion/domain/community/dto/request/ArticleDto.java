package competnion.domain.community.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class ArticleDto {

    @Getter
    @Builder
    public static class ArticlePostRequest {
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
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        private LocalDateTime startDate;
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        private LocalDateTime endDate;
        private Integer attendant;
        private List<Long> petIds;
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
