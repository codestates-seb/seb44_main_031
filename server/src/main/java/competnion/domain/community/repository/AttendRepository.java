package competnion.domain.community.repository;

import competnion.domain.community.entity.Attend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendRepository extends JpaRepository<Attend, Long> {
    long countByArticleId(Long articleId);
}
