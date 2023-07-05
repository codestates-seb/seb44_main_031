package competnion.domain.community.dto.request;

import competnion.domain.user.entity.User;
import lombok.*;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.*;

public class ArticleDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArticlePostDto {
        @NotBlank
        private String title;

        @NotBlank
        private String body;

        @NotBlank
        private String location;

        @NotBlank
        private LocalDateTime time;

        @NotBlank
        private int attendant;
    }

    public static class ArticlePatchDto {
        private Long articleId;
        @NotBlank
        private String title;

        @NotBlank
        private String body;

        @NotBlank
        private String location;

        @NotBlank
        private LocalDateTime time;

        @NotBlank
        private int attendant;
    }
}
