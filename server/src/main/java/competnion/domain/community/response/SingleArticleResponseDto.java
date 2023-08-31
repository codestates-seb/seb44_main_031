package competnion.domain.community.response;

import competnion.domain.community.dto.response.ArticleResponseDto;
import competnion.domain.pet.dto.response.PetResponse;
import competnion.domain.user.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SingleArticleResponseDto {
    private UserResponse.InArticleResponse owner;
    private ArticleResponseDto.OfSingleResponse article;
    private List<UserResponse.InArticleResponse> attendees;
}
