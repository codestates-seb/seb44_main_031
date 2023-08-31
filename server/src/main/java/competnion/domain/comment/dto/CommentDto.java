package competnion.domain.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class CommentDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        @NotBlank(message = "댓글의 내용은 공백이 아니어야 합니다")
        private String body;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private long commentId;
        private long userId;
        private String imgUrl;
        private String nickname;
        private String body;
        private LocalDateTime createdAt;
    }


}