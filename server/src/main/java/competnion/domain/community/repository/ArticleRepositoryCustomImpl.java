package competnion.domain.community.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import competnion.domain.community.dto.ArticleQueryDto;
import competnion.domain.community.dto.QArticleQueryDto;
import competnion.domain.community.entity.Article;
import competnion.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static competnion.domain.community.entity.QArticle.article;
import static competnion.domain.community.entity.QAttend.attend;
import static competnion.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    private BooleanExpression all(
            String keyword,
            int days
    ) {
        return days(days).and(keyword(keyword));
    }

    private BooleanExpression keyword(String keyword) {
        return keyword != null && !keyword.isEmpty() ? article.title.contains(keyword) : null;
    }

    private BooleanExpression days(int days) {
        return days == 7 ? article.date.between(LocalDateTime.now(), LocalDateTime.now().plusDays(7)) : null;
    }

    private BooleanExpression cursorId(Long cursorId) {
        return cursorId == null ? null : article.id.gt(cursorId);
    }

    @Override
    public List<ArticleQueryDto> findAllByKeywordAndDistanceAndDays(
            Point userPoint,
            String keyword,
            int days,
            Double distance,
//            String orderBy,
            long offset,
            int limit
    ) {
        return jpaQueryFactory
                .select(new QArticleQueryDto(article.title, article.date))
                .from(article)
                .where(
                        all(keyword, days),

                        Expressions.numberTemplate(
                                Double.class,
                                "ST_Distance_Sphere({0}, {1})", userPoint, article.point
                        ).loe(distance),

                        article.date.after(LocalDateTime.now())
                )
//                .orderBy(order(orderBy))
                .orderBy(article.date.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    @Override
    public List<ArticleQueryDto> findAllArticlesWrittenByUser(User userEntity) {
        return jpaQueryFactory
                .select(new QArticleQueryDto(article.id, article.title, article.date, article.createdAt))
                .from(article)
                .join(article.user, user)
                .where(
                        article.user.eq(userEntity)
                )
                .orderBy(article.createdAt.desc())
                .fetch();
    }




    @Override
    public Page<Article> findArticlesByConditionsWithCursorPaging(
                                                                    Long cursorId,
                                                                    Point userPoint,
                                                                    Double distance,
                                                                    Pageable pageable) {

        List<Article> articles = jpaQueryFactory
                                .select(article)
                                .from(article)
                                .where(

                                        cursorId(cursorId),


                                        Expressions.numberTemplate(
                                                Double.class,
                                                "ST_Distance_Sphere({0}, {1})", userPoint, article.point
                                        ).loe(distance),



                                        article.date.after(LocalDateTime.now())



                                )

                                .limit(pageable.getPageSize())
                                .fetch();



        return null;
    }
}
