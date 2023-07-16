package competnion.domain.community.repository;

import competnion.domain.community.entity.Attend;
import competnion.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendRepository extends JpaRepository<Attend, Long>, AttendRepositoryCustom {
    long countByArticleId(Long articleId);

    Optional<Attend> findByUserIdAndArticleId(Long userId, Long articleId);

    @Query (value = "select u from User u left join u.attends a "
                  + "on u.id = a.user.id "
                  + "where a.article.id = :articleId"  )
    List<User> findUsersFromAttendByArticleId (@Param("articleId") Long articleId);
}
