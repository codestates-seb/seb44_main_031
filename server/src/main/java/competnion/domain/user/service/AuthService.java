package competnion.domain.user.service;

import competnion.domain.user.dto.request.ResetPasswordRequest;
import competnion.domain.user.dto.request.SignUpRequest;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;

public interface AuthService {
    void signUp(SignUpRequest signUpRequest);
    void sendVerificationEmail(String email);
    void verifyEmail(String code, String email);
    Boolean isDuplicateEmail(String email);
    Boolean isDuplicateUsername(String username);
    void resetPassword(ResetPasswordRequest resetPasswordRequest);
    Point coordinateToPoint(Double latitude, Double longitude) throws ParseException;
}
