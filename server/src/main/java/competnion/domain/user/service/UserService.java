package competnion.domain.user.service;

import competnion.domain.auth.service.AuthService;
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
    private final UserRepository userRepository;
    private final AttendRepository attendRepository;
    private final ArticleRepository articleRepository;

    private final S3Util s3Util;
    private final AuthService authService;
    private final CoordinateUtil coordinateUtil;
    
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponse getProfile(final Long userId) {
        final User existsUser = returnExistsUserByIdOrThrow(userId);
        final List<PetResponse> petResponses = existsUser.getPets().stream()
                .map(PetResponse::of)
                .collect(Collectors.toList());
        return UserResponse.of(existsUser, petResponses);
    }

    public UpdateUsernameResponse updateUsername(final User user, final UpdateUsernameRequest request) {
        authService.checkMatchPassword(request.getPassword(), user.getPassword());

        authService.checkDuplicatedUsername(request.getNewNickname());

        user.updateNickname(request.getNewNickname());
        return UpdateUsernameResponse.of(user.getNickname());
    }

    public void resetPassword(final User user, final ResetPasswordRequest request) {
        authService.checkMatchPassword(request.getPassword(), user.getPassword());

        if (!request.getNewPassword().equals(request.getNewPasswordConfirm()))
            throw new BusinessLogicException(NEW_PASSWORD_NOT_MATCH);

        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    public UpdateAddressResponse updateAddress(final User user, final AddressRequest request) {
        final Point point = coordinateUtil.coordinateToPoint(request.getLongitude(), request.getLatitude());
        user.updateAddressAndCoordinates(request.getAddress(), point, request.getLatitude(), request.getLongitude());
        return UpdateAddressResponse.of(request);
    }

    public String uploadProfileImage(final User user, final MultipartFile image) {
        s3Util.isFileAnImageOrThrow(List.of(image));

        if (user.getImgUrl() != null) s3Util.deleteImage(user.getImgUrl());

        String imgUrl = s3Util.uploadImage(image);
        user.updateImgUrl(imgUrl);
        return imgUrl;
    }

    public List<ArticleQueryDto> findAllArticlesWrittenByUser(final User user) {
        return articleRepository.findAllArticlesWrittenByUser(user);
    }

    public List<ArticleQueryDto> findAllArticlesUserAttended(final User user) {
        return attendRepository.findAllArticlesUserAttended(user);
    }

    public User returnExistsUserByIdOrThrow(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(USER_NOT_FOUND));
    }
}
