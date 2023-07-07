package competnion.domain.user.controller;

import competnion.domain.user.dto.request.AddressRequest;
import competnion.domain.user.dto.request.UpdateUsernameRequest;
import competnion.domain.user.dto.response.UpdateAddressResponse;
import competnion.domain.user.dto.response.UpdateUsernameResponse;
import competnion.domain.user.dto.response.UserResponse;
import competnion.domain.user.service.UserService;
import competnion.global.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{user-id}")
    public ResponseEntity<Response> getUser(@PathVariable("user-id") final Long userId) {
        UserResponse userResponse = userService.getProfile(userId);

        return ResponseEntity.ok(Response.success(userResponse));
    }

    @PatchMapping("/update-address/{user-id}")
    public ResponseEntity<Response> updateAddressAndCoordinates(
            @PathVariable("user-id") final Long userId,
            @Valid @RequestBody      final AddressRequest addressRequest
    ) {
        UpdateAddressResponse addressResponse = userService.updateAddress(userId, addressRequest);

        return ResponseEntity.ok(Response.success(addressResponse));
    }

    @PatchMapping("/update-username")
    public ResponseEntity<Response> updateUsername(
            @PathVariable("userId") final Long userId,
            @Valid @RequestBody     final UpdateUsernameRequest updateUsernameRequest
    ) {
        UpdateUsernameResponse usernameResponse = userService.updateUsername(userId, updateUsernameRequest);

        return ResponseEntity.ok(Response.success(usernameResponse));
    }

    @PatchMapping("/image")
    public ResponseEntity<Response> uploadProfileImage(
            @PathVariable("userId") final Long userId,
            @RequestParam("image")  final MultipartFile image
    ) {
        String imgUrl = userService.uploadProfileImage(userId, image);

        return ResponseEntity.ok(Response.success(imgUrl));
    }
}
