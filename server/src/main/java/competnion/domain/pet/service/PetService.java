package competnion.domain.pet.service;

import competnion.domain.pet.dto.request.RegisterPetRequest;
import competnion.domain.pet.dto.request.UpdatePetInfoRequest;
import competnion.domain.pet.dto.response.PetResponse;
import competnion.domain.pet.entity.Pet;
import competnion.domain.pet.repository.PetRepository;
import competnion.domain.user.entity.User;
import competnion.global.exception.BusinessLogicException;
import competnion.infra.s3.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static competnion.global.exception.ExceptionCode.*;
import static java.util.Optional.ofNullable;

@Service
@Transactional
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final S3Util s3Util;

    public PetResponse registerPet(
            final User user,
            final RegisterPetRequest registerPetRequest,
            final MultipartFile image
    ) {
        hasSpaceForRegisterPetOrThrow(user.getId());
        s3Util.isFileAnImageOrThrow(image);
        String imgUrl = s3Util.uploadImage(image);

        Pet pet = savePet(user, registerPetRequest, imgUrl);
        user.addPet(pet);

        return PetResponse.of(pet);
    }

    public String updatePetImage(final User user, final Long petId, final MultipartFile image) {
        s3Util.isFileAnImageOrThrow(image);
        Pet pet = checkExistsPetOrThrow(user, petId);

        s3Util.deleteImage(pet.getImgUrl());
        String imgUrl = s3Util.uploadImage(image);
        pet.updateImgUrl(imgUrl);
        return imgUrl;
    }

    public Pet updatePetInfo(final User user, final Long petId, UpdatePetInfoRequest updatePetInfoRequest) {
        Pet pet = checkExistsPetOrThrow(user, petId);
        ofNullable(updatePetInfoRequest.getName()).ifPresent(pet::updateName);
        ofNullable(updatePetInfoRequest.getBirth()).ifPresent(pet::updateBirth);
        ofNullable(updatePetInfoRequest.getNeutralization()).ifPresent(pet::updateNeutralization);
        ofNullable(updatePetInfoRequest.getVaccine()).ifPresent(pet::updateVaccine);
        return pet;
    }

    public Pet checkExistsPetOrThrow(final User user, final Long petId) {
        Pet findPet = petRepository.findById(petId)
                .orElseThrow(() -> new BusinessLogicException(PET_NOT_FOUND));
        boolean petMatch = user.getPets().stream()
                .anyMatch(pet -> pet.equals(findPet));

        if (!petMatch) throw new BusinessLogicException(PET_NOT_MATCH);
        return findPet;
    }

    public void checkUserHasPet(User user) {
        if (user.getPets().size() == 0) throw new BusinessLogicException(PET_NOT_FOUND);
    }

    public void hasSpaceForRegisterPetOrThrow (final Long userId) {
        final Integer count = petRepository.countByUserId(userId);
        if (count >= 3) throw new BusinessLogicException(FORBIDDEN);
    }

    private Pet savePet(User user, RegisterPetRequest registerPetRequest, String imgUrl) {
        return petRepository.save(Pet.RegisterPet()
                .name(registerPetRequest.getName())
                .birth(registerPetRequest.getBirth())
                .gender(registerPetRequest.getGender())
                .neutralization(registerPetRequest.getNeutralization())
                .imgUrl(imgUrl)
                .vaccine(registerPetRequest.getVaccine())
                .user(user)
                .build());
    }
}
