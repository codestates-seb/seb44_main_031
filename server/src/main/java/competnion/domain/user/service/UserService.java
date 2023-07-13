package competnion.domain.user.service;

import competnion.domain.pet.dto.response.PetResponse;
import competnion.domain.pet.repository.PetRepository;
import competnion.domain.user.dto.request.AddressRequest;
import competnion.domain.user.dto.request.SignUpRequest;
import competnion.domain.user.dto.response.UpdateAddressResponse;
import competnion.domain.user.dto.response.UpdateUsernameResponse;
import competnion.domain.user.dto.response.UserResponse;
import competnion.domain.user.entity.User;
import competnion.domain.user.repository.UserRepository;
import competnion.global.exception.BusinessLogicException;
import competnion.global.util.CoordinateUtil;
import competnion.infra.s3.S3Util;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static competnion.global.exception.ExceptionCode.USER_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final CoordinateUtil coordinateUtil;
    private final S3Util s3Util;

    @Transactional(readOnly = true)
    public UserResponse getProfile(final Long userId) {
        User existsUser = returnExistsUserByIdOrThrow(userId);
        List<PetResponse> pets = petRepository.findAllByUserId(userId)
                .stream()
                .map(PetResponse::of)
                .collect(Collectors.toList());
        return UserResponse.of(existsUser, pets);
    }

    @Transactional
    public UpdateUsernameResponse updateUsername(
            final User user,
            final String username
    ) {
        user.updateUsername(username);
        return UpdateUsernameResponse.of(user.getUsername());
    }

    @Transactional
    public UpdateAddressResponse updateAddress(
            final User user,
            final AddressRequest addressRequest
    ) {
        Point point = coordinateUtil.coordinateToPoint(addressRequest.getLatitude(), addressRequest.getLongitude());
        user.updateAddressAndCoordinates(addressRequest.getAddress(), point);
        return UpdateAddressResponse.of(
                addressRequest.getLatitude(), addressRequest.getLongitude(), addressRequest.getAddress()
        );
    }

    @Transactional
    public String uploadProfileImage(
            final User user,
            final MultipartFile image
    ) {
        s3Util.isFileAnImageOrThrow(image);
        if (user.getImgUrl() != null)
            s3Util.deleteImage(user.getImgUrl());
        String imgUrl = s3Util.uploadImage(image);

        user.updateImgUrl(imgUrl);

        return imgUrl;
    }

    public User returnExistsUserByIdOrThrow(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(USER_NOT_FOUND));
    }

    public Boolean checkExistsUserByUsername(final String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    public Boolean checkExistsUserByEmail(final String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    public Boolean checkEmailValidate(final User user, final String email) {
        return user.getEmail().equals(email);
    }

    public void deleteUser(final User user) {
        userRepository.delete(user);
    }

    public void saveUser(Point point, SignUpRequest signUpRequest, String encode, List<String> roles) {
        userRepository.save(User.SignUp()
                .email(signUpRequest.getEmail())
                .username(signUpRequest.getUsername())
                .password(encode)
                .address(signUpRequest.getAddress())
                .point(point)
                .latitude(signUpRequest.getLatitude())
                .longitude(signUpRequest.getLongitude())
                .roles(roles)
                .build());
    }
}
