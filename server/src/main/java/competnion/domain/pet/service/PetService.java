package competnion.domain.pet.service;

import competnion.domain.pet.dto.request.RegisterPetRequest;
import competnion.domain.pet.dto.request.UpdatePetInfoRequest;
import competnion.domain.pet.dto.response.PetResponse;
import competnion.domain.pet.entity.Breed;
import competnion.domain.pet.entity.Pet;
import competnion.domain.pet.repository.BreedRepository;
import competnion.domain.pet.repository.PetRepository;
import competnion.domain.user.entity.User;
import competnion.global.exception.BusinessLogicException;
import competnion.infra.s3.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static competnion.global.exception.ExceptionCode.*;
import static java.util.Optional.ofNullable;

@Service
@Transactional
@RequiredArgsConstructor
public class PetService {

    private final S3Util s3Util;

    private final PetRepository petRepository;
    private final BreedRepository breedRepository;

    public PetResponse registerPet(
            final User user,
            final RegisterPetRequest request,
            final MultipartFile image
    ) {
        checkSpaceForRegisterPetOrThrow(user.getId());
        s3Util.isFileAnImageOrThrow(List.of(image));
        String imgUrl = s3Util.uploadImage(image);

        Breed breed = returnExistsBreedOrThrow(request.getBreedId());
        Pet pet = savePet(user, request, imgUrl, breed);
        user.addPet(pet);
        return PetResponse.of(pet);
    }

    public String updatePetImage(final User user, final Long petId, final MultipartFile image) {
        s3Util.isFileAnImageOrThrow(List.of(image));
        Pet pet = returnExistsPetOrThrow(petId);
        checkPetMatchUser(user, pet);

        s3Util.deleteImage(pet.getImgUrl());
        String imgUrl = s3Util.uploadImage(image);
        pet.updateImgUrl(imgUrl);
        return imgUrl;
    }

    public void updatePetInfo(final User user, final Long petId, UpdatePetInfoRequest request) {
        Pet pet = returnExistsPetOrThrow(petId);
        Breed breed = returnExistsBreedOrThrow(request.getBreedId());
        checkPetMatchUser(user, pet);

        ofNullable(request.getName()).ifPresent(pet::updateName);
        ofNullable(request.getBirth()).ifPresent(pet::updateBirth);
        ofNullable(request.getGender()).ifPresent(pet::updateGender);
        ofNullable(request.getNeutralization()).ifPresent(pet::updateNeutralization);
        ofNullable(breed).ifPresent(pet::updateBreed);
        ofNullable(request.getMbti()).ifPresent(pet::updateMbti);
    }

    public void deletePet(User user, Long petId) {
        Pet pet = returnExistsPetOrThrow(petId);
        checkPetMatchUser(user, pet);
        checkValidPetOrThrow(pet);
        petRepository.delete(pet);
        user.getPets().remove(pet);
    }

    public Breed returnExistsBreedOrThrow(final Long breedId) {
        return breedRepository.findById(breedId)
                .orElseThrow(() -> new BusinessLogicException(BREED_NOT_FOUND));
    }

    public Pet returnExistsPetOrThrow(final Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new BusinessLogicException(PET_NOT_FOUND));
    }

    public List<Pet> returnExistsPetsOrThrow(final List<Long> petIds) {
        final List<Pet> pets = new ArrayList<>();
        for (Long petId : petIds) {
            pets.add(returnExistsPetOrThrow(petId));
        }
        return pets;
    }

    public void checkValidPetOrThrow(final Pet pet) {
        if (pet.getArticle() != null) throw new BusinessLogicException(PET_ALREADY_ATTENDED);
    }

    public void checkUserHasPetOrThrow(final User user) {
        if (user.getPets().size() == 0) throw new BusinessLogicException(PET_NOT_FOUND);
    }

    public void checkPetMatchUser(final User user, final Pet findPet) {
        final boolean petMatch = user.getPets().stream()
                .anyMatch(pet -> pet.equals(findPet));
        if (!petMatch) throw new BusinessLogicException(PET_NOT_MATCH);
    }

    private void checkSpaceForRegisterPetOrThrow (final Long userId) {
        final Integer count = petRepository.countByUserId(userId);
        if (count >= 3) throw new BusinessLogicException(FORBIDDEN);
    }

    private Pet savePet(
            final User user,
            final RegisterPetRequest request,
            final String imgUrl,
            final Breed breed
    ) {
        return petRepository.save(Pet.RegisterPet()
                .name(request.getName())
                .birth(request.getBirth())
                .gender(request.getGender())
                .neutralization(request.getNeutralization())
                .imgUrl(imgUrl)
                .user(user)
                .mbti(request.getMbti())
                .breed(breed)
                .build());
    }
}
