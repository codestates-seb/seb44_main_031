package competnion.domain.community.mapper;

import competnion.domain.comment.dto.CommentDto;
import competnion.domain.comment.entity.Comment;
import competnion.domain.community.dto.response.ArticleResponseDto;
import competnion.domain.community.entity.Article;
import competnion.domain.community.response.SingleArticleResponseDto;
import competnion.domain.pet.dto.response.PetResponse;
import competnion.domain.pet.entity.Pet;
import competnion.domain.user.dto.response.UserResponse;
import competnion.domain.user.entity.User;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ArticleMapper {


    default SingleArticleResponseDto articleToSingleArticleResponse(List<String> imgUrl,Article article,List<User> users) {

        User user = article.getUser();


        UserResponse owner = UserResponse.inArticleResponse(user, petsToPetSimpleNameResponse(user.getPets()));

        ArticleResponseDto articles = ArticleResponseDto.ofSingleArticleResponse(imgUrl,article,commentsToCommentResponses(article.getComments()));

        List<UserResponse> attendees = users.stream()
                .map(attendee -> UserResponse.inArticleResponse(attendee,petsToPetSimpleNameResponse(attendee.getPets())))
                .collect(Collectors.toList());


        return new SingleArticleResponseDto(owner,articles,attendees);
    }

    default List<PetResponse> petsToPetSimpleNameResponse (List<Pet> pets) {
        return pets.stream()
                    .map(PetResponse::petName)
                    .collect(Collectors.toList());
    }


    default List<CommentDto.Response> commentsToCommentResponses(List<Comment> comments) {
        return comments.stream()
                        .map(comment -> new CommentDto.Response(
                                comment.getCommentId(),
                                comment.getUser().getId(),
                                comment.getUser().getUsername(),
                                comment.getBody(),
                                comment.getCreatedAt()))
                        .collect(Collectors.toList());
    }
}
