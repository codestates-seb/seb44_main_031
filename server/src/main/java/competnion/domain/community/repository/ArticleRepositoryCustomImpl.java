package competnion.domain.community.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
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
//            int days,
            Double distance,
//            String orderBy,
            long offset,
            int limit
    ) {
        return jpaQueryFactory
                .select(new QArticleQueryDto(article.title, article.date))
                .from(article)
                .where(
                        all(keyword),
                        Expressions.numberTemplate(Double.class,
                                "ST_Distance_Sphere({0}, {1})", userPoint, article.point).loe(distance),
                        article.date.after(LocalDateTime.now())
                )
//                .orderBy(order(orderBy))
                .orderBy(article.date.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    private BooleanExpression all(
            String keyword
//            int days
    ) {
//        return days(days).and(keyword(keyword));
        return (keyword(keyword));
    }

    private BooleanExpression keyword(String keyword) {
        return keyword != null && !keyword.isEmpty()
                ? article.title.contains(keyword)
                : null;
    }

//    private BooleanExpression days(int days) {
//        return days == 7
//                ? article.date.between(LocalDateTime.now(), LocalDateTime.now().plusDays(7))
//                : null;
//    }

//    private OrderSpecifier<?> order(String order) {
//        if (order.equals("meetingDateDesc")) {
//            return article.date.desc();
//        }
//
//        if (order.equals("createdDateDesc")) {
//            return article.createdAt.desc();
//        }
//
//        return article.date.desc();
//    }
}