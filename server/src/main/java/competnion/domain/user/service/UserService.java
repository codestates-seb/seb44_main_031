package competnion.domain.user.service;

import competnion.domain.user.dto.request.AddressRequest;
import competnion.domain.user.dto.request.UpdateUsernameRequest;
import competnion.domain.user.dto.response.UpdateAddressResponse;
import competnion.domain.user.dto.response.UpdateUsernameResponse;
import competnion.domain.user.dto.response.UserResponse;

public interface UserService {
    UserResponse getProfile(Long userId);
    UpdateUsernameResponse updateUsername(Long userId, UpdateUsernameRequest updateUsernameRequest);
    UpdateAddressResponse updateAddress(Long userId, AddressRequest addressRequest);
//    String uploadProfileImage(Long userId, MultipartFile image);
}
