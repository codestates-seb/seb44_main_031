package competnion.domain.comment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class CommentDto {


    @Getter
    @Setter
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

        private String imgurl;

        private String username;

        private String body;

        private LocalDateTime createdAt;
    }


}