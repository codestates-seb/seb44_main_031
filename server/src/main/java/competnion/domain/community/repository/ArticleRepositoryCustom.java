package competnion.domain.community.repository;

import competnion.domain.community.dto.ArticleQueryDto;
import competnion.domain.community.dto.response.ArticleResponseDto;
import competnion.domain.community.entity.Article;
import competnion.domain.user.entity.User;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepositoryCustom {
    Page<Article> findAllByKeywordAndDistanceAndDays(
            Point userPoint,
            String keyword,
            Integer days,
            Double distance,
            Pageable pageable
    );

    List<ArticleQueryDto> findAllArticlesWrittenByUser(
            User user
    );
    List<Article> findAllArticlesOpenWrittenByUser (
            User user
    );

    void findDuplicateMeetingDate(User user, LocalDateTime startDate, LocalDateTime endDate);
    List<Article> findArticlesOpen();

}
