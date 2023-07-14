package competnion.domain.community.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import competnion.domain.community.entity.Article;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static competnion.domain.community.entity.QArticle.article;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Article> findAllByKeywordAndDistance(
            Point userPoint,
            String keyword,
            Double distance,
            long offset,
            int limit
    ) {
        BooleanBuilder keywordPredicate = new BooleanBuilder();

        if (keyword != null && !keyword.isEmpty()) {
            keywordPredicate.and(article.title.containsIgnoreCase(keyword));
        }

        return jpaQueryFactory
                .selectFrom(article)
                .where(
                        keywordPredicate,
                        Expressions.numberTemplate(Double.class,
                                "ST_Distance_Sphere({0}, {1})", userPoint, article.point).loe(distance),
                        article.date.after(LocalDateTime.now())
                )
                .orderBy(article.date.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }
}
