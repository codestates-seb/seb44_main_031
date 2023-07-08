package competnion.domain.user.service;

import competnion.domain.pet.dto.response.PetResponse;
import competnion.domain.pet.entity.Pet;
import competnion.domain.pet.repository.PetRepository;
import competnion.domain.user.dto.request.AddressRequest;
import competnion.domain.user.dto.request.UpdateUsernameRequest;
import competnion.domain.user.dto.response.UpdateAddressResponse;
import competnion.domain.user.dto.response.UpdateUsernameResponse;
import competnion.domain.user.dto.response.UserResponse;
import competnion.domain.user.entity.User;
import competnion.domain.user.repository.UserRepository;
import competnion.global.exception.BusinessException;
import competnion.global.util.CoordinateUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static competnion.global.exception.ErrorCode.INVALID_INPUT_VALUE;
import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final CoordinateUtil coordinateUtil;
//    private final S3Util s3Util;

    @Transactional(readOnly = true)
    @Override
    public UserResponse getProfile(final Long userId) {
        User existsUser = isExistsUser(userId);
        List<Pet> pets = petRepository.findAllByUserId(userId);
        List<PetResponse> petList = pets.stream()
                .map(PetResponse::of)
                .collect(Collectors.toList());

        return UserResponse.of(existsUser, petList);
    }

    @Transactional
    @Override
    public UpdateUsernameResponse updateUsername(
            final Long userId,
            final UpdateUsernameRequest updateUsernameRequest
    ) {
        return UpdateUsernameResponse.of("임시");
    }

    @Transactional
    @Override
    public UpdateAddressResponse updateAddress(
            final Long userId,
            final AddressRequest addressRequest
    ) {
        User existsUser = isExistsUser(userId);
        Point point = coordinateUtil.coordinateToPoint(addressRequest.getLatitude(), addressRequest.getLongitude());

        existsUser.setAddress(addressRequest.getAddress());
        existsUser.setPoint(point);

        return UpdateAddressResponse.of(
                addressRequest.getLatitude(), addressRequest.getLongitude(), addressRequest.getAddress()
        );
    }

//    @Override
//    public String uploadProfileImage(
//            final Long userId,
//            final MultipartFile image
//    ) {
//        isFileAnImage(image);
//        User existsUser = isExistsUser(userId);
//        String imgUrl = s3Util.uploadImage(image);
//
//        if (existsUser.getImgUrl() != null)
//            s3Util.deleteImage(existsUser.getImgUrl());
//
//        existsUser.updateImgUrl(imgUrl);
//
//        return imgUrl;
//    }

    private User isExistsUser(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(INVALID_INPUT_VALUE));
    }

    private void isFileAnImage(final MultipartFile image) {
        String fileExtension = FilenameUtils.getExtension(requireNonNull(image.getOriginalFilename()).toLowerCase());
        if (!fileExtension.equals("jpg") && !fileExtension.equals("jpeg") && !fileExtension.equals("png"))
            throw new BusinessException(INVALID_INPUT_VALUE);
    }

//    private Long getAuthenticatedUserId() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = userRepository.findByEmail(authentication.getName())
//                .orElseThrow(() -> new BusinessException(INVALID_INPUT_VALUE));
//        return user.getId();
//    }
}
