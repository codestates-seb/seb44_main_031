package competnion.domain.community.repository;

import competnion.domain.user.entity.User;

import java.time.ZonedDateTime;

public interface AttendRepositoryCustom {
    void findAttendeeDuplicateMeetingDate(User user, ZonedDateTime startDate, ZonedDateTime endDate);
}
