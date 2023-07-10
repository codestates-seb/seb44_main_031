package competnion.domain.pet.service;

import competnion.domain.pet.dto.response.PetResponse;
import competnion.domain.user.dto.request.RegisterPetRequest;

public interface PetService {
    PetResponse registerPet(Long userId, RegisterPetRequest registerPetRequest);
}
