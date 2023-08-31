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

import java.util.List;

import static competnion.global.exception.ExceptionCode.*;
import static java.lang.String.format;
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
        checkLimitForRegisterPetOrThrow(user.getId());
        s3Util.checkFileIsImageOrThrow(List.of(image));
        String imgUrl = s3Util.uploadImage(image);

        Breed breed = getBreedOrThrow(request.getBreedId());
        Pet pet = savePet(user, request, imgUrl, breed);
        user.addPet(pet);
        return PetResponse.of(pet);
    }

    public String updatePetImage(final User user, final Long petId, final MultipartFile image) {
        s3Util.checkFileIsImageOrThrow(List.of(image));
        Pet pet = returnExistsPetOrThrow(petId);
        checkIsUsersPetOrThrow(user, pet);

        s3Util.deleteImage(pet.getImgUrl());
        String imgUrl = s3Util.uploadImage(image);
        pet.updateImgUrl(imgUrl);
        return imgUrl;
    }

    public void updatePetInfo(final User user, final Long petId, UpdatePetInfoRequest request) {
        Pet pet = returnExistsPetOrThrow(petId);
        Breed breed = getBreedOrThrow(request.getBreedId());
        checkIsUsersPetOrThrow(user, pet);
        editPetInfo(request, pet, breed);
    }

    private void editPetInfo(UpdatePetInfoRequest request, Pet pet, Breed breed) {
        ofNullable(request.getName()).ifPresent(pet::updateName);
        ofNullable(request.getBirth()).ifPresent(pet::updateBirth);
        ofNullable(request.getGender()).ifPresent(pet::updateGender);
        ofNullable(request.getIsNeutralized()).ifPresent(pet::updateNeutralization);
        ofNullable(breed).ifPresent(pet::updateBreed);
        ofNullable(request.getMbti()).ifPresent(pet::updateMbti);
    }

    public void deletePet(User user, Long petId) {
        Pet pet = returnExistsPetOrThrow(petId);
        checkIsUsersPetOrThrow(user, pet);
        checkValidPetOrThrow(pet);
        petRepository.delete(pet);
        user.getPets().remove(pet);
    }

    public Breed getBreedOrThrow(final Long breedId) {
        return breedRepository.findById(breedId)
                .orElseThrow(() -> new BusinessLogicException(BREED_NOT_FOUND));
    }

    public Pet returnExistsPetOrThrow(final Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new BusinessLogicException(PET_NOT_FOUND));
    }

    public List<Pet> returnExistsPetsOrThrow(final List<Long> petIds) {
        return petRepository.findAllById(petIds);
    }

    public void checkValidPetOrThrow(final Pet pet) {
        if (pet.getArticle() != null)
            throw new BusinessLogicException(
                    PET_ALREADY_ATTENDED,
                    format("%s는 산책 참여 중입니다.!", pet.getName())
            );
    }

    public void checkUserHasPetOrThrow(final User user) {
        if (user.getPets().size() == 0)
            throw new BusinessLogicException(PET_NOT_FOUND, "펫을 등록해주세요!");
    }

    public void checkIsUsersPetOrThrow(final User user, final Pet findPet) {
        final boolean petMatch = user.getPets().stream()
                .anyMatch(pet -> pet.equals(findPet));
        if (!petMatch)
            throw new BusinessLogicException(NOT_USERS_PET,
                    format("%s 는 %s 님의 반려견이 아닙니다!", findPet.getName(), user.getNickname())
            );
    }

    private void checkLimitForRegisterPetOrThrow(final Long userId) {
        final Integer count = petRepository.countByUserId(userId);
        if (count >= 4) throw new BusinessLogicException(CANNOT_REGISTER_MORE_DOG);
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
                .neutralization(request.getIsNeutralized())
                .imgUrl(imgUrl)
                .user(user)
                .mbti(request.getMbti())
                .breed(breed)
                .build());
    }
}
