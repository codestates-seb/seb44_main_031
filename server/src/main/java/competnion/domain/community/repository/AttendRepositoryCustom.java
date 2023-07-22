package competnion.domain.community.repository;

import competnion.domain.community.dto.ArticleQueryDto;
import competnion.domain.community.entity.Attend;
import competnion.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface AttendRepositoryCustom {
    List<ArticleQueryDto> findAllArticlesUserAttended(
            User user
    );
    List<Attend> findAllArticlesUserAttendedExceptClosed(
            User user
    );
    void findAttendeeDuplicateMeetingDate(User user, LocalDateTime startDate, LocalDateTime endDate);
}