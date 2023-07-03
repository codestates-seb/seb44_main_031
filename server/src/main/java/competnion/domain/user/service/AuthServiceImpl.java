package competnion.domain.user.service;

import competnion.domain.user.dto.request.ResetPasswordRequest;
import competnion.domain.user.dto.request.SignUpRequest;
import competnion.domain.user.entity.User;
import competnion.domain.user.repository.UserRepository;
import competnion.global.exception.BusinessException;
import competnion.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static competnion.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;

    /**
     * TODO : 예외처리 에러코드 추가 필요
     */
    @Override
    public void signUp(SignUpRequest signUpRequest) {
        if (isDuplicateEmail(signUpRequest.getEmail()))
            throw new BusinessException(INVALID_INPUT_VALUE);

        if (isDuplicateUsername(signUpRequest.getUsername()))
            throw new BusinessException(INVALID_INPUT_VALUE);
    }

    @Override
    public void sendVerificationEmail(String email) {

    }

    @Override
    public void verifyEmail(String code, String email) {

    }

    @Override
    public Boolean isDuplicateEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public Boolean isDuplicateUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {

    }

    /**
     * TODO : 예외처리 에러코드 추가 필요
     */
    @Override
    public Point coordinateToPoint(Double latitude, Double longitude) throws ParseException {
        if (latitude != null && longitude != null)
            return (Point) new WKTReader().read(String.format("POINT(%s %s)", latitude, longitude));
        else
            throw new BusinessException(INVALID_INPUT_VALUE);
    }
}
