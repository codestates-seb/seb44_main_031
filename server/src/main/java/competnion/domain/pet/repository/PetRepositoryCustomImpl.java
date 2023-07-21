package competnion.domain.pet.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import competnion.domain.pet.entity.Pet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static competnion.domain.community.entity.QArticle.article;
import static competnion.domain.pet.entity.QPet.pet;
import static competnion.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class PetRepositoryCustomImpl implements PetRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Pet> findAllByUserId(Long userId) {
        return jpaQueryFactory
                .selectFrom(pet)
                .join(pet.user, user).fetchJoin()
                .where(pet.user.id.eq(userId))
                .fetch();
    }

    @Override
    public List<Pet> findParticipatingPetsByUserIdAndArticleId(Long userId, Long articleId) {
        return jpaQueryFactory
                .selectFrom(pet)
                .join(pet.user, user).fetchJoin()
                .join(pet.article, article).fetchJoin()
                .where(
                        pet.user.id.eq(userId),
                        pet.article.id.eq(articleId)
                )
                .fetch();
    }
}
