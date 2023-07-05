package competnion.domain.user.service;

import competnion.domain.user.dto.request.EditLocationRequest;
import competnion.domain.user.dto.request.EditUsernameRequest;
import competnion.domain.user.dto.request.RegisterPetRequest;
import competnion.domain.user.dto.response.PetResponse;
import competnion.domain.user.dto.response.UserResponse;
import competnion.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public UserResponse getProfile(Long userId) {
        return null;
    }

    @Override
    public UserResponse editUsername(Long userId, EditUsernameRequest editUsernameRequest) {
        return null;
    }

    @Override
    public UserResponse editLocation(Long userId, EditLocationRequest editLocationRequest) {
        return null;
    }

    @Override
    public String uploadProfileImage(Long userId, MultipartFile image) {
        return null;
    }

    @Override
    public PetResponse registerPet(Long userId, RegisterPetRequest registerPetRequest) {
        return null;
    }

    @Override
    public String uploadPetImage(Long userId, Long petId, MultipartFile image) {
        return null;
    }
}
