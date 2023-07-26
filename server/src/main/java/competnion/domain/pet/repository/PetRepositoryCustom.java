package competnion.domain.pet.repository;

import competnion.domain.community.dto.ArticleQueryDto;
import competnion.domain.pet.entity.Pet;
import competnion.domain.user.entity.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PetRepositoryCustom {
    List<Pet> findAllByUserId(Long userId);
    List<Pet> findParticipatingPetsByUserIdAndArticleId (
            @Param("userId") Long userId,
            @Param("articleId") Long articleId
    );
    ArticleQueryDto findArticlePetAttended(User user, Long petId);
}
