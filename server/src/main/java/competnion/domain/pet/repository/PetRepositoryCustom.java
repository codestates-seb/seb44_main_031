package competnion.domain.pet.repository;

import competnion.domain.pet.entity.Pet;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PetRepositoryCustom {
    List<Pet> findAllByUserId(Long userId);
    List<Pet> findParticipatingPetsByUserIdAndArticleId (
            @Param("userId") Long userId,
            @Param("articleId") Long articleId
    );
}
