package competnion.domain.community.repository;

import competnion.domain.community.dto.ArticleQueryDto;
import competnion.domain.community.entity.Article;
import competnion.domain.user.entity.User;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleRepositoryCustom {
    List<ArticleQueryDto> findAllByKeywordAndDistanceAndDays(
            Point userPoint,
            String keyword,
            int days,
            Double distance,
//            String orderBy,
            long offset,
            int limit);

    List<ArticleQueryDto> findAllArticlesWrittenByUser(
            User user
    );

    Page<Article> findArticlesByConditionsWithCursorPaging(
            Long cursorId,
            Point userPoint,
            Double distance,
            Pageable pageable
    );
}
