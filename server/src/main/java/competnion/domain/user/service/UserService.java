package competnion.domain.user.service;

import competnion.domain.user.dto.request.EditLocationRequest;
import competnion.domain.user.dto.request.EditUsernameRequest;
import competnion.domain.user.dto.request.RegisterPetRequest;
import competnion.domain.user.dto.response.PetResponse;
import competnion.domain.user.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserResponse getProfile(Long userId);
    UserResponse editUsername(Long userId, EditUsernameRequest editUsernameRequest);
    UserResponse editLocation(Long userId, EditLocationRequest editLocationRequest);
    String uploadProfileImage(Long userId, MultipartFile image);
    PetResponse registerPet(Long userId, RegisterPetRequest registerPetRequest);
    String uploadPetImage(Long userId, Long petId, MultipartFile image);

}
