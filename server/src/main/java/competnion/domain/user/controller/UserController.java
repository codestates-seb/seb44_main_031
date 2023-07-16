package competnion.domain.user.controller;

import competnion.domain.community.dto.ArticleQueryDto;
import competnion.domain.user.annotation.UserContext;
import competnion.domain.user.annotation.ValidUsername;
import competnion.domain.user.dto.request.AddressRequest;
import competnion.domain.user.dto.request.ResetPasswordRequest;
import competnion.domain.user.dto.request.UpdateUsernameRequest;
import competnion.domain.user.dto.response.UpdateAddressResponse;
import competnion.domain.user.dto.response.UpdateUsernameResponse;
import competnion.domain.user.dto.response.UserResponse;
import competnion.domain.user.entity.User;
import competnion.domain.user.service.UserService;
import competnion.global.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 마이페이지
    @GetMapping("/{user-id}")
    public Response<UserResponse> getUser(@Positive @PathVariable("user-id") final Long userId) {
        return Response.success(userService.getProfile(userId));
    }

    // 주소 변경
    @PatchMapping("/address")
    public Response<UpdateAddressResponse> updateAddressAndCoordinates(
            @UserContext        final User user,
            @Valid @RequestBody final AddressRequest request
    ) {
        return Response.success(userService.updateAddress(user, request));
    }

    // 유저네임 수정
    @PatchMapping("/username")
    public Response<UpdateUsernameResponse> updateUsername(
            @UserContext        final User user,
            @Valid @RequestBody final UpdateUsernameRequest request
    ) {
        return Response.success(userService.updateUsername(user, request));
    }

    // 비밀번호 재설정
    @PatchMapping("/password")
    public Response<Void> updatePassword(
            @UserContext        final User user,
            @Valid @RequestBody final ResetPasswordRequest request
    ) {
        userService.resetPassword(user, request);
        return Response.success();
    }

    // 프로필 이미지 업로드
    @PatchMapping("/image")
    public Response<String> uploadProfileImage(
            @UserContext             final User user,
            @RequestPart("image")    final MultipartFile image
    ) {
        return Response.success(userService.uploadProfileImage(user, image));
    }

    @GetMapping("/get-articles-written-by")
    public Response<List<ArticleQueryDto>> findAllArticlesWrittenByUser(@UserContext final User user) {
        return Response.success(userService.findAllArticlesWrittenByUser(user));
    }

    @GetMapping("/get-articles-attended")
    public Response<List<ArticleQueryDto>> findAllArticlesUserAttended(@UserContext final User user) {
        return Response.success(userService.findAllArticlesUserAttended(user));
    }
}
