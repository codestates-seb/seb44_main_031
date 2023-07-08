package competnion.domain.pet.service;

import competnion.domain.pet.dto.response.PetResponse;
import competnion.domain.pet.entity.Pet;
import competnion.domain.pet.repository.PetRepository;
import competnion.domain.user.dto.request.RegisterPetRequest;
import competnion.domain.user.entity.User;
import competnion.domain.user.repository.UserRepository;
import competnion.global.exception.BusinessException;
import competnion.infra.s3.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static competnion.global.exception.ErrorCode.INVALID_INPUT_VALUE;
import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService{

    private final PetRepository petRepository;
    private final UserRepository userRepository;
//    private final S3Util s3Util;

    @Override
    public PetResponse registerPet(
            final Long userId,
            final RegisterPetRequest registerPetRequest
    ) {
        User existsUser = isExistsUser(userId);
        hasSpaceForMoreDog(userId);
//        isFileAnImage(registerPetRequest.getImage());
        Pet pet = postPet(existsUser, registerPetRequest);
        petRepository.save(pet);
        existsUser.addPet(pet);
        return PetResponse.of(pet);
    }

    private void hasSpaceForMoreDog (Long userId) {
        Integer count = petRepository.countByUserId(userId);
        if (count >= 3) throw new BusinessException(INVALID_INPUT_VALUE);
    }

    private User isExistsUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(INVALID_INPUT_VALUE));
    }

//    private void isFileAnImage(MultipartFile image) {
//        String fileExtension = FilenameUtils.getExtension(requireNonNull(image.getOriginalFilename()).toLowerCase());
//        if (!fileExtension.equals("jpg") && !fileExtension.equals("jpeg") && !fileExtension.equals("png"))
//            throw new BusinessException(INVALID_INPUT_VALUE);
//    }

    private Pet postPet(User user, RegisterPetRequest registerPetRequest) {
        return Pet.RegisterPet()
                .name(registerPetRequest.getName())
                .birth(registerPetRequest.getBirth())
                .gender(registerPetRequest.getGender())
                .isNeutered(registerPetRequest.getIsNeutered())
//                .imgUrl(s3Util.uploadImage(registerPetRequest.getImage()))
                .imgUrl("test.jpg")
                .inoculated(registerPetRequest.getInoculated())
                .user(user)
                .build();
    }

}
