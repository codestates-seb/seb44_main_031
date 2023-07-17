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
import competnion.global.exception.ExceptionCode;
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
            final RegisterPetRequest registerPetRequest,
            final MultipartFile image
    ) {
        checkSpaceForRegisterPetOrThrow(user.getId());
        s3Util.isFileAnImageOrThrow(image);
        String imgUrl = s3Util.uploadImage(image);

        Breed breed = returnExistsBreedOrThrow(registerPetRequest.getBreedId());
        Pet pet = savePet(user, registerPetRequest, imgUrl, breed);
        user.addPet(pet);
        return PetResponse.of(pet);
    }

    public String updatePetImage(final User user, final Long petId, final MultipartFile image) {
        s3Util.isFileAnImageOrThrow(image);
        Pet pet = returnExistsPetOrThrow(petId);
        checkPetMatchUser(user, pet);

        s3Util.deleteImage(pet.getImgUrl());
        String imgUrl = s3Util.uploadImage(image);
        pet.updateImgUrl(imgUrl);
        return imgUrl;

    }

    public Pet updatePetInfo(final User user, final Long petId, UpdatePetInfoRequest updatePetInfoRequest) {
        Pet pet = returnExistsPetOrThrow(petId);
        checkPetMatchUser(user, pet);
        ofNullable(updatePetInfoRequest.getName()).ifPresent(pet::updateName);
        ofNullable(updatePetInfoRequest.getBirth()).ifPresent(pet::updateBirth);
        ofNullable(updatePetInfoRequest.getNeutralization()).ifPresent(pet::updateNeutralization);
        ofNullable(updatePetInfoRequest.getVaccine()).ifPresent(pet::updateVaccine);
        return pet;
    }

    public void deletePet(User user, Long petId) {
        Pet pet = returnExistsPetOrThrow(petId);
        checkPetMatchUser(user, pet);
        checkValidPetOrThrow(pet);
        petRepository.delete(pet);
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
                .vaccine(request.getVaccine())
                .user(user)
                .mbti(request.getMbti())
                .breed(breed)
                .build());
    }
}
