package competnion.domain.community.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import competnion.domain.community.dto.ArticleQueryDto;
import competnion.domain.community.dto.QArticleQueryDto;
import competnion.domain.community.entity.Attend;
import competnion.domain.user.entity.User;
import competnion.global.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import static competnion.domain.community.entity.ArticleStatus.OPEN;
import static competnion.domain.community.entity.QArticle.article;
import static competnion.domain.community.entity.QAttend.attend;
import static competnion.domain.user.entity.QUser.user;
import static competnion.global.exception.ExceptionCode.NOT_VALID_MEETING_DATE;

@Repository
@RequiredArgsConstructor
public class AttendRepositoryCustomImpl implements AttendRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ArticleQueryDto> findAllArticlesUserAttended(User userEntity) {
        return jpaQueryFactory
                .select(new QArticleQueryDto(article.id, article.title, article.startDate, article.createdAt))
                .from(attend)
                .join(attend.article, article)
                .join(attend.user, user)
                .where(
                        attend.user.eq(userEntity)
                )
                .distinct()
                .fetch();
    }
    @Override
    public List<Attend> findAllArticlesUserAttendedExceptClosed(User userEntity) {
        return jpaQueryFactory
                .selectFrom(attend)
                .join(attend.article, article)
                .join(attend.user, user)
                .where(
                        attend.user.eq(userEntity),
                        article.articleStatus.eq(OPEN)
                )
                .distinct()
                .fetch();
    }
    @Override
    public void findAttendeeDuplicateMeetingDate(User userEntity, ZonedDateTime startDate, ZonedDateTime endDate) {
        List<Attend> attends = jpaQueryFactory
                .selectFrom(attend)
                .join(attend.article, article)
                .join(attend.user, user)
                .where(
                        attend.user.eq(userEntity),
                        article.startDate.between(startDate, endDate)
                                .or(article.endDate.between(startDate, endDate))
                )
                .fetch();

        if (attends.size() > 0) throw new BusinessLogicException(NOT_VALID_MEETING_DATE);
    }
}
