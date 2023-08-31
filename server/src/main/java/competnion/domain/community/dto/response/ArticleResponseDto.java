package competnion.domain.community.dto.response;

import competnion.domain.comment.dto.CommentDto;
import competnion.domain.community.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class ArticleResponseDto {
        private Long articleId;
        private String username;
        private String title;
        private String body;
        private ZonedDateTime startDate;
        private ZonedDateTime endDate;
        private String location;
        private int attendant;
        private List<String> imageUrls;
        private List<CommentDto.Response> comments;

        // ArticleResponseDto of
        private ArticleResponseDto(Long articleId, String username, String title, String body, List<String> imageUrls) {
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
                private ZonedDateTime startDate;
                private ZonedDateTime endDate;
                private Double latitude;
                private Double longitude;
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
                                article.getPoint().getY(),
                                article.getPoint().getX(),
                                article.getLocation(),
                                article.getAttendCapacity(),
                                comments
                        );
                }
        }

        @Getter
        public static class OfMultiResponse {
                private List<String> imageUrls;
                private Long articleId;
                private ZonedDateTime startDate;
                private long endDate;
                private String title;
                private String body;
                private String location;
                private Double latitude; //y 좌표
                private Double longitude; // x 좌표
                private int attendant;
                private int lefts;
                private Boolean isViewerJoining;

                private OfMultiResponse(List<String> imageUrls, Long articleId, ZonedDateTime startDate, long endDate, String title, String body, String location, Double latitude, Double longitude, int attendant, int lefts, Boolean isViewerJoining) {
                        this.imageUrls = imageUrls;
                        this.articleId = articleId;
                        this.startDate = startDate;
                        this.endDate = endDate;
                        this.title = title;
                        this.body = body;
                        this.location = location;
                        this.latitude = latitude;
                        this.longitude = longitude;
                        this.attendant = attendant;
                        this.lefts = lefts;
                        this.isViewerJoining = isViewerJoining;
                }

                public static ArticleResponseDto.OfMultiResponse getResponse(List<String> imageUrls,
                                                                             Article article,
                                                                             int lefts,
                                                                             Boolean checkJoin

                )
                {
                        return new ArticleResponseDto.OfMultiResponse(
                                imageUrls,
                                article.getId(),
                                article.getStartDate(),
                                Duration.between(article.getStartDate(),article.getEndDate()).getSeconds()/60,
                                article.getTitle(),
                                article.getBody(),
                                article.getLocation(),
                                article.getPoint().getY(),
                                article.getPoint().getX(),
                                article.getAttendCapacity(),
                                lefts,
                                checkJoin

                        );
                }
        }
}