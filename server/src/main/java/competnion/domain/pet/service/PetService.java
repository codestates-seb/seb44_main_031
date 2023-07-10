package competnion.domain.pet.service;

import competnion.domain.pet.dto.response.PetResponse;
import competnion.domain.pet.entity.Pet;
import competnion.domain.pet.repository.PetRepository;
import competnion.domain.user.dto.request.RegisterPetRequest;
import competnion.domain.user.entity.User;
import competnion.domain.user.service.UserService;
import competnion.global.exception.BusinessException;
import competnion.infra.s3.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static competnion.global.exception.ErrorCode.COUNTDOG;
import static competnion.global.exception.ErrorCode.INVALID_INPUT_VALUE;

@Service
@Transactional
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final UserService userService;
    private final S3Util s3Util;

    public PetResponse registerPet(
            final Long userId,
            final RegisterPetRequest registerPetRequest,
            final MultipartFile image
    ) {
        User user = userService.returnExistsUserByIdOrThrow(userId);
        hasSpaceForRegisterPetOrThrow(userId);
        s3Util.isFileAnImageOrThrow(image);
        String imgUrl = s3Util.uploadImage(image);

        Pet pet = savePet(user, registerPetRequest, imgUrl);
        user.addPet(pet);

        return PetResponse.of(pet);
    }

    public String updatePetImage(final Long userId, final Long petId, final MultipartFile image) {
        s3Util.isFileAnImageOrThrow(image);
        Pet pet = checkExistsPetOrThrow(userId, petId);

        s3Util.deleteImage(pet.getImgUrl());
        String imgUrl = s3Util.uploadImage(image);
        pet.updateImgUrl(imgUrl);
        return imgUrl;
    }

    /**
     * TODO : 리팩토링 필요
     */
    public Pet checkExistsPetOrThrow(final Long userId, final Long petId) {
        Pet findPet = petRepository.findById(petId)
                .orElseThrow(() -> new BusinessException(INVALID_INPUT_VALUE));
        User user = userService.returnExistsUserByIdOrThrow(userId);

        boolean petMatch = user.getPets().stream()
                .anyMatch(pet -> pet.equals(findPet));
        if (!petMatch) throw new BusinessException(INVALID_INPUT_VALUE);

        return findPet;
    }

    public void hasSpaceForRegisterPetOrThrow (final Long userId) {
        final Integer count = petRepository.countByUserId(userId);
        if (count >= 3) throw new BusinessException(INVALID_INPUT_VALUE);
    }

    private Pet savePet(User user, RegisterPetRequest registerPetRequest, String imgUrl) {
        return petRepository.save(Pet.RegisterPet()
                .name(registerPetRequest.getName())
                .birth(registerPetRequest.getBirth())
                .gender(registerPetRequest.getGender())
                .isNeutered(registerPetRequest.getIsNeutered())
                .imgUrl(imgUrl)
                .inoculated(registerPetRequest.getInoculated())
                .user(user)
                .build());
    }
}
