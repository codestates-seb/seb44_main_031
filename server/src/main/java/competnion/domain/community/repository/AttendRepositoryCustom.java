package competnion.domain.community.repository;

import competnion.domain.community.dto.ArticleQueryDto;
import competnion.domain.user.entity.User;

import java.util.List;

public interface AttendRepositoryCustom {
    List<ArticleQueryDto> findAllArticlesUserAttended(
            User user
    );
}
