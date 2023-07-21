package competnion.domain.community.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import competnion.domain.community.dto.ArticleQueryDto;
import competnion.domain.community.dto.QArticleQueryDto;
import competnion.domain.community.entity.Article;
import competnion.domain.user.entity.User;
import competnion.global.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static competnion.domain.community.entity.ArticleStatus.OPEN;
import static competnion.domain.community.entity.QArticle.article;
import static competnion.domain.user.entity.QUser.user;
import static competnion.global.exception.ExceptionCode.NOT_VALID_MEETING_DATE;



@Repository
@RequiredArgsConstructor
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    private BooleanExpression all(
            String keyword,
            Integer days
    ) {
        return days(days).and(keyword(keyword));
    }

    private BooleanExpression keyword(String keyword) {
        return keyword != null && !keyword.isEmpty() ? article.title.contains(keyword) : null;
    }

    private BooleanExpression days(Integer days) {

        return days != null ? article.startDate.between(LocalDateTime.now(), LocalDateTime.now().plusDays(days)) : null;

    }


    @Override
    public Page<Article> findAllByKeywordAndDistanceAndDays(
            Point userPoint,
            String keyword,
            int days,
            Double distance,
            Pageable pageable
    ) {
        List<Article> content = jpaQueryFactory
                .selectFrom(article)
                .where(
                        all(keyword, days),

                        Expressions.numberTemplate(
                                Double.class,
                                "ST_Distance_Sphere({0}, {1})", userPoint, article.point
                        ).loe(distance),

                        article.articleStatus.eq(OPEN),
                        article.startDate.after(LocalDateTime.now())
                )
                .orderBy(article.startDate.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운트 쿼리 (전체 자료 갯수 구하기)
        long total = jpaQueryFactory
                    .selectFrom(article)
                .where(all(keyword, days),

                        Expressions.numberTemplate(
                                Double.class,
                                "ST_Distance_Sphere({0}, {1})", userPoint, article.point
                        ).loe(distance),

                        article.articleStatus.eq(OPEN),
                        article.startDate.after(LocalDateTime.now())
                )
                .fetchCount();

        return new PageImpl<>(content,pageable,total);
    }

    @Override
    public List<ArticleQueryDto> findAllArticlesWrittenByUser(User userEntity) {
        return jpaQueryFactory
                .select(new QArticleQueryDto(article.id, article.title, article.startDate, article.createdAt))
                .from(article)
                .join(article.user, user)
                .where(
                        article.user.eq(userEntity)
                )
                .orderBy(article.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Article> findAllArticlesOpenWrittenByUser(User userEntity) {
        return jpaQueryFactory
                .selectFrom(article)
                .join(article.user, user)
                .where(
                        article.articleStatus.eq(OPEN)
                )
                .fetch();
    }


    @Override
    public void findDuplicateMeetingDate(User userEntity, LocalDateTime starDate, LocalDateTime endDate) {
        List<Article> articles = jpaQueryFactory
                .selectFrom(article)
                .join(article.user, user)
                .where(
                        article.user.eq(userEntity),
                        article.startDate.between(starDate, endDate)
                                .or(article.endDate.between(starDate, endDate))
                )
                .fetch();

        if (articles.size() > 0) throw new BusinessLogicException(NOT_VALID_MEETING_DATE);
    }

    @Override
    public List<Article> findArticlesOpen() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime passed = now.minusMinutes(30);
        return jpaQueryFactory
                .selectFrom(article)
                .where(
                        article.articleStatus.eq(OPEN),
                        article.endDate.before(passed)
                )
                .fetch();
    }
}
