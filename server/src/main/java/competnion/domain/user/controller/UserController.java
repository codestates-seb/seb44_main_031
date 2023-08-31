package competnion.domain.user.controller;

import competnion.domain.community.dto.ArticleQueryDto;
import competnion.global.common.annotation.AuthenticatedUser;
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
    @GetMapping("/info/{user-id}")
    public Response<UserResponse> getUser(@Positive @PathVariable("user-id") final Long userId) {
        return Response.success(userService.getProfile(userId));
    }

    // 주소 변경
    @PatchMapping("/info/address")
    public Response<UpdateAddressResponse> updateAddressAndCoordinates(
            @AuthenticatedUser final User user,
            @Valid @RequestBody final AddressRequest request
    ) {
        return Response.success(userService.updateAddress(user, request));
    }

    // 유저네임 수정
    @PatchMapping("/info/username")
    public Response<UpdateUsernameResponse> updateUsername(
            @AuthenticatedUser final User user,
            @Valid @RequestBody final UpdateUsernameRequest request
    ) {
        return Response.success(userService.updateUsername(user, request));
    }

    // 비밀번호 재설정
    @PatchMapping("/info/password")
    public Response<Void> updatePassword(
            @AuthenticatedUser final User user,
            @Valid @RequestBody final ResetPasswordRequest request
    ) {
        userService.resetPassword(user, request);
        return Response.success();
    }

    // 프로필 이미지 업로드
    @PatchMapping("/info/profile-image")
    public Response<String> uploadProfileImage(
            @AuthenticatedUser final User user,
            @RequestPart("image")    final MultipartFile image
    ) {
        return Response.success(userService.uploadProfileImage(user, image));
    }

    // 작성한 게시글 조회
    @GetMapping("/info/articles-written-by")
    public Response<List<ArticleQueryDto>> findAllArticlesWrittenByUser(@AuthenticatedUser final User user) {
        return Response.success(userService.findAllArticlesWrittenByUser(user));
    }

    // 반려견이 참여중인 게시글 조회
    @GetMapping("/info/articles-attended/{pet-id}")
    public Response<ArticleQueryDto> findAllArticlesPetAttended(
            @AuthenticatedUser final User user,
            @Positive @PathVariable("pet-id") final Long petId
    ) {
        return Response.success(userService.findArticlesPetAttended(user, petId));
    }
}
