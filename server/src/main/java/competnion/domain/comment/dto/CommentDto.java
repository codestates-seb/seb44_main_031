//package competnion.domain.comment.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Positive;
//import java.time.LocalDateTime;
//
//public class CommentDto {
//
//    @AllArgsConstructor
//    @Getter
//    @Setter
//    public static class Post {
//
//        @Positive
//        private long articleId;
//
//        @NotBlank(message = "댓글의 내용은 공백이 아니어야 합니다")
//        private String body;
//    }
//
//    @AllArgsConstructor
//    @Getter
//    @Setter
//    public static class Patch {
//
//        @NotBlank(message = "댓글의 내용은 공백이 아니어야 합니다")
//        private String body;
//    }
//
//    @AllArgsConstructor
//    @Getter
//    public static class Response {
//
//        @NotBlank(message = "댓글의 내용은 공백이 아니어야 합니다")
//        private String body;
//
//        private LocalDateTime createdAt;
//
//        private LocalDateTime modifiedAt;
//    }
//
//
//}
