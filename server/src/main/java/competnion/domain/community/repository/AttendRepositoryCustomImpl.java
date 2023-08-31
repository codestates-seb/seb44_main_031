package competnion.domain.community.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import competnion.domain.community.dto.QArticleQueryDto;
import competnion.domain.community.entity.Attend;
import competnion.domain.user.entity.User;
import competnion.global.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

import static competnion.domain.community.entity.QArticle.article;
import static competnion.domain.pet.entity.QPet.pet;
import static competnion.domain.community.entity.QAttend.attend;
import static competnion.domain.user.entity.QUser.user;
import static competnion.global.exception.ExceptionCode.NOT_VALID_MEETING_DURATION;

@Repository
@RequiredArgsConstructor
public class AttendRepositoryCustomImpl implements AttendRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

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

        if (attends.size() > 0) throw new BusinessLogicException(NOT_VALID_MEETING_DURATION, "이미 겹치는 시간에 참여 하였습니다.");
    }
}
