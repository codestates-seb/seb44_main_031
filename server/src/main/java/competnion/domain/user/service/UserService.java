package competnion.domain.user.service;

import competnion.domain.user.dto.request.AddressRequest;
import competnion.domain.user.dto.request.EditUsernameRequest;
import competnion.domain.user.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserResponse getProfile(Long userId);
    UserResponse editUsername(Long userId, EditUsernameRequest editUsernameRequest);
    UserResponse editAddress(Long userId, AddressRequest addressRequest);
    String uploadProfileImage(Long userId, MultipartFile image);
}
