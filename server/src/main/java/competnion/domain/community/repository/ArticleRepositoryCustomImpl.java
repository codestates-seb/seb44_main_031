package competnion.domain.community.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import competnion.domain.community.dto.ArticleQueryDto;
import competnion.domain.community.dto.QArticleQueryDto;
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
    public List<ArticleQueryDto> findAllByKeywordAndDistance(
            Point userPoint,
            String keyword,
            int days,
            Double distance,
            long offset,
            int limit
    ) {
        BooleanBuilder keywordPredicate = new BooleanBuilder();
        BooleanBuilder daysPredicate = new BooleanBuilder();

        if (keyword != null && !keyword.isEmpty())
            keywordPredicate.and(article.title.containsIgnoreCase(keyword));

        if (days == 7)
            daysPredicate.and(article.date.between(LocalDateTime.now(), LocalDateTime.now().plusDays(days)));


        return jpaQueryFactory
                .select(new QArticleQueryDto(article.title, article.date))
                .from(article)
                .where(
                        keywordPredicate,
                        daysPredicate,
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
