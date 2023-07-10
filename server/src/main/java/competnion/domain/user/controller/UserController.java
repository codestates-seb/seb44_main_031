package competnion.domain.user.controller;

import competnion.domain.user.annotation.ValidUsername;
import competnion.domain.user.dto.request.AddressRequest;
import competnion.domain.user.dto.request.UpdateUsernameRequest;
import competnion.domain.user.dto.response.UpdateAddressResponse;
import competnion.domain.user.dto.response.UpdateUsernameResponse;
import competnion.domain.user.dto.response.UserResponse;
import competnion.domain.user.service.UserService;
import competnion.global.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * TODO : 커스텀 어노테이션으로 검증하기
     */
    @GetMapping("/{user-id}")
    public Response<UserResponse> getUser(@Positive @PathVariable("user-id") final Long userId) {
        return Response.success(userService.getProfile(userId));
    }

    @PatchMapping("/address/{user-id}")
    public Response<UpdateAddressResponse> updateAddressAndCoordinates(
            @Positive @PathVariable("user-id") final Long userId,
            @Valid @RequestBody                final AddressRequest addressRequest
    ) {
        return Response.success(userService.updateAddress(userId, addressRequest));
    }

    @PatchMapping("/username/{user-id}")
    public Response<UpdateUsernameResponse> updateUsername(
            @Positive @PathVariable("user-id")        final Long userId,
            @ValidUsername @RequestParam("username") final String username
    ) {
        return Response.success(userService.updateUsername(userId, username));
    }

    @PatchMapping("/image/{user-id}")
    public Response<String> uploadProfileImage(
            @PathVariable("user-id") final Long userId,
            @RequestPart("image")    final MultipartFile image
    ) {
        return Response.success(userService.uploadProfileImage(userId, image));
    }
}
