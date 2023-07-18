package competnion.domain.pet.repository;

import competnion.domain.pet.entity.Pet;
import org.aspectj.weaver.ast.Literal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findAllByUserId(Long userId);
    Integer countByUserId(Long userId);

    List<Pet> findAllByArticleId(Long articleId);
}
