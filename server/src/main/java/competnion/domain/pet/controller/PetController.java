package competnion.domain.pet.controller;

import competnion.domain.pet.dto.request.RegisterPetRequest;
import competnion.domain.pet.dto.request.UpdatePetInfoRequest;
import competnion.domain.pet.dto.response.PetResponse;
import competnion.domain.pet.service.PetService;
import competnion.domain.user.annotation.UserContext;
import competnion.domain.user.entity.User;
import competnion.global.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@Validated
@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    //    펫 등록 할때 이미지도 같이 등록
    @PostMapping("/register")
    public Response<PetResponse> registerPet(
            @UserContext                   final User user,
            @Valid @RequestPart("request") final RegisterPetRequest registerPetRequest,
            @RequestPart("image")          final MultipartFile image
    ) {
        return Response.success(petService.registerPet(user, registerPetRequest, image));
    }

    // 펫 이미지 수정
    @PatchMapping("/image/{pet-id}")
    public Response<String> updatePetImage(
            @UserContext                      final User user,
            @Positive @PathVariable("pet-id") final Long petId,
            @RequestPart("image")             final MultipartFile image
    ) {
        return Response.success(petService.updatePetImage(user, petId, image));
    }

    // 펫 정보 수정(일괄)
    @PatchMapping("/information/{pet-id}")
    public Response<Void> updatePetInfo(
            @UserContext                      final User user,
            @Positive @PathVariable("pet-id") final Long petId,
            @Valid @RequestBody               final UpdatePetInfoRequest updatePetInfoRequest
    ) {
        petService.updatePetInfo(user, petId, updatePetInfoRequest);
        return Response.success();
    }

    // 펫 삭제
    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{pet-id}")
    public Response<Void> deletePet(@UserContext final User user, @Positive @PathVariable("pet-id") final Long petId) {
        petService.deletePet(user, petId);
        return Response.success();
    }
}
