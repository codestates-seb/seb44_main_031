package competnion.domain.community.repository;

import competnion.domain.community.entity.Article;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {
//    @Query("SELECT a FROM Article a WHERE " +
//            "FUNCTION('ST_DISTANCE', a.location, :userPoint) * 1000 <= 3000")
//    Page<Article> findNearbyArticles(@Param("userPoint") Point userPoint,Pageable pageable);


    Article findArticleById(long articleId);

}
