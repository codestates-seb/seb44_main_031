package competnion.domain.community.mapper;

import competnion.domain.comment.dto.CommentDto;
import competnion.domain.comment.entity.Comment;
import competnion.domain.community.dto.response.ArticleResponseDto;
import competnion.domain.community.entity.Article;
import competnion.domain.community.response.MultiArticleResponse;
import competnion.domain.community.response.SingleArticleResponseDto;
import competnion.domain.pet.dto.response.PetResponse;
import competnion.domain.pet.entity.Pet;
import competnion.domain.user.dto.response.UserResponse;
import competnion.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ArticleMapper {


    default SingleArticleResponseDto articleToSingleArticleResponse(List<String> imgUrl,Article article,List<User> users) {

        User user = article.getUser(); // 작성자


        List<Pet> dogs = selectJoiningPets(article,user);


        UserResponse.InArticleResponse owner = UserResponse.InArticleResponse.getResponse(user, petsToPetSimpleNameResponse(dogs));


        ArticleResponseDto.OfSingleResponse articles = ArticleResponseDto.OfSingleResponse.getSingleResponse(imgUrl,article,commentsToCommentResponses(article.getComments()));

        List<UserResponse.InArticleResponse> attendees = users.stream()
                .map(attendee -> UserResponse.InArticleResponse.getResponse(attendee,petsToPetSimpleNameResponse(selectJoiningPets(article,attendee))))
                .collect(Collectors.toList());


        return new SingleArticleResponseDto(owner,articles,attendees);
    }


    default MultiArticleResponse articleToMultiArticleResponses(List<ArticleResponseDto.OfMultiResponse> responses,
                                                                User user,
                                                                Page page) {


        UserResponse.OfMultiArticleResponse userinfo = UserResponse.OfMultiArticleResponse.getResponse(user);




        return new MultiArticleResponse(responses,userinfo,page);
    }

    default List<PetResponse.ForArticleResponse> petsToPetSimpleNameResponse (List<Pet> pets) {
        return pets.stream()
                    .map(PetResponse.ForArticleResponse::getSimplePetName)
                    .collect(Collectors.toList());
    }


    default List<CommentDto.Response> commentsToCommentResponses(List<Comment> comments) {
        return comments.stream()
                        .map(comment -> new CommentDto.Response(
                                comment.getCommentId(),
                                comment.getUser().getId(),
                                comment.getUser().getImgUrl(),
                                comment.getUser().getNickname(),
                                comment.getBody(),
                                comment.getCreatedAt()))
                        .collect(Collectors.toList());
    }

    default List<Pet> selectJoiningPets (Article article,User user) {

        return user.getPets().stream()
                .filter(pet -> pet.getArticle() != null)
                .filter(pet -> pet.getArticle().getId().equals(article.getId()))
                .collect(Collectors.toList());
    }
}