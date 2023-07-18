package competnion.domain.community.response;

import competnion.domain.community.dto.response.ArticleResponseDto;
import competnion.domain.user.dto.response.UserResponse;
import competnion.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@Getter
public class MultiArticleResponse {
    private List <ArticleResponseDto.OfMultiResponse> article;
    private UserResponse.OfMultiArticleResponse userInfo;


}
