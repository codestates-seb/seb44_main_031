package competnion.domain.user.service;

import competnion.domain.community.dto.ArticleQueryDto;
import competnion.domain.community.repository.ArticleRepository;
import competnion.domain.community.repository.AttendRepository;
import competnion.domain.pet.dto.response.PetResponse;
import competnion.domain.pet.repository.PetRepository;
import competnion.domain.user.dto.request.AddressRequest;
import competnion.domain.user.dto.request.ResetPasswordRequest;
import competnion.domain.user.dto.request.SignUpRequest;
import competnion.domain.user.dto.request.UpdateUsernameRequest;
import competnion.domain.user.dto.response.UpdateAddressResponse;
import competnion.domain.user.dto.response.UpdateUsernameResponse;
import competnion.domain.user.dto.response.UserResponse;
import competnion.domain.user.entity.User;
import competnion.domain.user.repository.UserRepository;
import competnion.global.exception.BusinessLogicException;
import competnion.global.exception.ExceptionCode;
import competnion.global.util.CoordinateUtil;
import competnion.infra.s3.S3Util;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static competnion.global.exception.ExceptionCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final AttendRepository attendRepository;
    private final ArticleRepository articleRepository;

    private final S3Util s3Util;
    private final CoordinateUtil coordinateUtil;
    
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponse getProfile(final Long userId) {
        User existsUser = returnExistsUserByIdOrThrow(userId);
        List<PetResponse> pets = petRepository.findAllByUserId(userId)
                .stream()
                .map(PetResponse::of)
                .collect(Collectors.toList());
        return UserResponse.of(existsUser, pets);
    }

    public UpdateUsernameResponse updateUsername(final User user, final UpdateUsernameRequest request) {
        checkMatchPassword(request.getPassword(), user.getPassword());

        if (userRepository.findByNickname(request.getNewNickname()).isPresent())
            throw new BusinessLogicException((DUPLICATE_USERNAME));

        user.updateUsername(request.getNewNickname());
        return UpdateUsernameResponse.of(user.getNickname());
    }

    public void resetPassword(final User user, final ResetPasswordRequest request) {
        checkMatchPassword(request.getPassword(), user.getPassword());

        if (!request.getNewPassword().equals(request.getNewPasswordConfirm()))
            throw new BusinessLogicException(NEW_PASSWORD_NOT_MATCH);

        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    public UpdateAddressResponse updateAddress(final User user, final AddressRequest request) {
        Point point = coordinateUtil.coordinateToPoint(request.getLatitude(), request.getLongitude());
        user.updateAddressAndCoordinates(request.getAddress(), point);
        return UpdateAddressResponse.of(request);
    }

    public String uploadProfileImage(final User user, final MultipartFile image) {
        s3Util.isFileAnImageOrThrow(image);

        if (user.getImgUrl() != null) s3Util.deleteImage(user.getImgUrl());

        String imgUrl = s3Util.uploadImage(image);
        user.updateImgUrl(imgUrl);
        return imgUrl;
    }

    public User returnExistsUserByIdOrThrow(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(USER_NOT_FOUND));
    }

    public List<ArticleQueryDto> findAllArticlesWrittenByUser(final User user) {
        return articleRepository.findAllArticlesWrittenByUser(user);
    }

    public List<ArticleQueryDto> findAllArticlesUserAttended(final User user) {
        return attendRepository.findAllArticlesUserAttended(user);
    }

    public Boolean checkExistsUserByUsername(final String username) {
        return userRepository.findByNickname(username).isEmpty();
    }

    public Boolean checkExistsUserByEmail(final String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    public Boolean checkEmailValidate(final User user, final String email) {
        return user.getEmail().equals(email);
    }

    private void checkMatchPassword(final String password, final String encodePassword) {
        boolean matches = passwordEncoder.matches(password, encodePassword);
        if (!matches) throw new BusinessLogicException(PASSWORD_NOT_MATCH);
    }

    public void saveUser(
            final Point point,
            final SignUpRequest request,
            final String encode,
            final List<String> roles
    ) {
        userRepository.save(User.signUp()
                .email(request.getEmail())
                .nickname(request.getUsername())
                .password(encode)
                .address(request.getAddress())
                .point(point)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .imgUrl("https://mybucketforpetmily.s3.ap-northeast-2.amazonaws.com/dog.png")
                .roles(roles)
                .build());
    }
}
