package competnion.domain.community.repository;

import competnion.domain.community.dto.ArticleQueryDto;
import competnion.domain.community.entity.Attend;
import competnion.domain.user.entity.User;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

public interface AttendRepositoryCustom {
//    ArticleQueryDto findArticlesPetAttended(
//            User user,
//            Long petId
//    );
    List<Attend> findAllArticlesUserAttendedExceptClosed(
            User user
    );
    void findAttendeeDuplicateMeetingDate(User user, ZonedDateTime startDate, ZonedDateTime endDate);
}
