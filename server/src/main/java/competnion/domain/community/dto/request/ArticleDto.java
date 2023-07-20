package competnion.domain.community.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

public class ArticleDto {

    @Getter
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
        @Pattern(regexp = "^(30|60|90|120|150|180)$")
        private String endDate;
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
