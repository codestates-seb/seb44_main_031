package competnion.domain.community.repository;

import competnion.domain.community.dto.ArticleQueryDto;
import competnion.domain.user.entity.User;
import org.locationtech.jts.geom.Point;

import java.util.List;

public interface ArticleRepositoryCustom {
    List<ArticleQueryDto> findAllByKeywordAndDistance(
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

    List<ArticleQueryDto> findAllArticlesUserAttended(
            User user
    );
}
