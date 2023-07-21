package competnion.domain.pet.repository;

import competnion.domain.pet.entity.Pet;
import org.aspectj.weaver.ast.Literal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long>, PetRepositoryCustom {

    List<Pet> findAllById(List<Long> petIds);
    Integer countByUserId(Long userId);
    List<Pet> findAllByArticleId(Long articleId);
}
