package competnion.domain.community.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@Getter
public class MultiArticleResponse<T> {
    private List<T> data;
    private PageInfo pageInfo;

    public MultiArticleResponse(List<T> data, Page page){
        this.data = data;
        this.pageInfo = new PageInfo(page.getNumber()+1,
                page.getSize(),page.getTotalElements(),page.getTotalPages());
    }
}
