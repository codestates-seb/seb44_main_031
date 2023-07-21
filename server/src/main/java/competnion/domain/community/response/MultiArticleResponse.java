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
    private List <ArticleResponseDto.OfMultiResponse> articles;
    private UserResponse.OfMultiArticleResponse userInfo;
    private Pageinfo pageinfo;

    public MultiArticleResponse(
            List<ArticleResponseDto.OfMultiResponse> articles,
            UserResponse.OfMultiArticleResponse userInfo,
            Page page) {

        this.articles = articles;
        this.userInfo = userInfo;
        this.pageinfo = new Pageinfo(page.getNumber()+1,page.getTotalPages());
    }

}
