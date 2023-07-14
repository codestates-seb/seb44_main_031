package competnion.domain.community.repository;

import competnion.domain.community.entity.Article;
import org.locationtech.jts.geom.Point;

import java.util.List;

public interface ArticleRepositoryCustom {
    List<Article> findAllByKeywordAndDistance(Point userPoint, String keyword, Double distance, long offset, int limit);
}
