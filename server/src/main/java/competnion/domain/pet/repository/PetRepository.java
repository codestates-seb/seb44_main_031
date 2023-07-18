package competnion.domain.pet.repository;

import competnion.domain.pet.entity.Pet;
import org.aspectj.weaver.ast.Literal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findAllByUserId(Long userId);
    Integer countByUserId(Long userId);

    List<Pet> findAllByArticleId(Long articleId);

    @Query (value = "select p from Pet p "
                  + "where p.user.id = :userId "
                  + "and p.article.id = :articleId")
    List<Pet> findParticipatingPetsByUserIdAndArticleId (
            @Param("userId") Long userId,
            @Param("articleId") Long articleId
    );
}
