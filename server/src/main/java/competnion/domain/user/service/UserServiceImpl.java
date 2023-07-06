package competnion.domain.user.service;

import competnion.domain.user.dto.request.AddressRequest;
import competnion.domain.user.dto.request.EditUsernameRequest;
import competnion.domain.user.dto.response.UserResponse;
import competnion.domain.user.entity.User;
import competnion.domain.user.repository.UserRepository;
import competnion.global.exception.BusinessException;
import competnion.infra.s3.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static competnion.global.exception.ErrorCode.INVALID_INPUT_VALUE;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final S3Util s3Util;

    @Override
    public UserResponse getProfile(final Long userId) {

        return null;
    }

    @Override
    public UserResponse editUsername(final Long userId, final EditUsernameRequest editUsernameRequest) {
        return null;
    }

    @Override
    public UserResponse editAddress(final Long userId, final AddressRequest addressRequest) {
        User  existsUser = isExistsUser(userId);
        Point point = coordinateToPoint(addressRequest.getLatitude(), addressRequest.getLongitude());
        existsUser.updateAddressAndCoordinates(addressRequest.getAddress(), point);
        return UserResponse.of(existsUser);
    }

    @Override
    public String uploadProfileImage(final Long userId, final MultipartFile image) {
        User existsUser = isExistsUser(userId);
        String imgUrl = s3Util.uploadImage(image);

        if (existsUser.getImgUrl() != null)
            s3Util.deleteImage(existsUser.getImgUrl());

        existsUser.updateImgUrl(imgUrl);
        return imgUrl;
    }

    public User isExistsUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(INVALID_INPUT_VALUE));
    }

    public Point coordinateToPoint(Double latitude, Double longitude) {
        if (latitude != null && longitude != null) {
            try {
                return (Point) new WKTReader().read(String.format("POINT(%s %s)", latitude, longitude));
            } catch (ParseException e) {
                throw new BusinessException(INVALID_INPUT_VALUE);
            }
        } else {
            throw new BusinessException(INVALID_INPUT_VALUE);
        }
    }
}
