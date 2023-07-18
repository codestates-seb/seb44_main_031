package competnion.domain.community.dto.response;

import competnion.domain.comment.dto.CommentDto;
import competnion.domain.community.entity.Article;
import competnion.domain.user.dto.response.UserResponse;
import competnion.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class ArticleResponseDto {
        private Long articleId;
        private String username;
        private String title;
        private String body;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String location;
        private int attendant;
        private List<String> imageUrls;
        private List<CommentDto.Response> comments;





        // ArticleResponseDto of
        public ArticleResponseDto(Long articleId, String username, String title, String body, List<String> imageUrls) {
                this.articleId = articleId;
                this.username = username;
                this.title = title;
                this.body = body;
                this.imageUrls = imageUrls;
        }


        public static ArticleResponseDto of(Article article, List<String> imageUrls) {
                return new ArticleResponseDto(
                        article.getId(),
                        article.getUser().getNickname(),
                        article.getTitle(),
                        article.getBody(),
                        imageUrls
                );
        }

        @Getter
        @AllArgsConstructor
        public static class OfSingleResponse {

                private List<String> imageUrls;
                private Long articleId;
                private String username;
                private String title;
                private String body;
                private LocalDateTime startDate;
                private LocalDateTime endDate;
                private String location;
                private int attendant;
                private List<CommentDto.Response> comments;



                public static ArticleResponseDto.OfSingleResponse getSingleResponse (List<String> imageUrls,
                                                                                     Article article,
                                                                                     List<CommentDto.Response> comments) {
                        return new OfSingleResponse(
                                imageUrls,
                                article.getId(),
                                article.getUser().getNickname(),
                                article.getTitle(),
                                article.getBody(),
                                article.getStartDate(),
                                article.getEndDate(),
                                article.getLocation(),
                                article.getAttendant(),
                                comments
                        );
                }



        }

        @Getter
        @AllArgsConstructor
        public static class OfMultiResponse {

                private List<String> imageUrls;
                private Long articleId;
                private LocalDateTime startDate;
                private LocalDateTime endDate;
                private String title;
                private String body;
                private String location;
                private int attandant;
                private int lefts;
                private Boolean isSelectedToJoinByViewer;


//                public static ArticleResponseDto.OfMultiResponse getResponse(List<String> imageUrls, Article article) {
//                        return new ArticleResponseDto.OfMultiResponse(
//                                imageUrls,
//                                article.getId(),
//                                article.getStartDate(),
//                                article.getEndDate(),
//                                article.getTitle(),
//                                article.getBody(),
//                                article.getLocation(),
//                                article.getAttendant(),
//                                article.getLefts(),
//                                article.getIselectedToJoinByViewer()
//                        );
//                }
        }
}