package competnion.domain.community.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import competnion.domain.community.dto.ArticleQueryDto;
import competnion.domain.community.dto.QArticleQueryDto;
import competnion.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static competnion.domain.community.entity.QArticle.article;
import static competnion.domain.community.entity.QAttend.attend;
import static competnion.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class AttendRepositoryCustomImpl implements AttendRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ArticleQueryDto> findAllArticlesUserAttended(User userEntity) {
        return jpaQueryFactory
                .select(new QArticleQueryDto(article.id, article.title, article.date, article.createdAt))
                .from(attend)
                .join(attend.article, article)
                .join(attend.user, user)
                .where(
                        attend.user.eq(userEntity)
                )
                .distinct()
                .fetch();
    }
}
