package competnion.domain.pet.repository;

import competnion.domain.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findAllByUserId(Long userId);
    Integer countByUserId(Long userId);
}
