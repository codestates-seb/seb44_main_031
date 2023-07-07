package competnion.domain.pet.controller;

import competnion.domain.pet.dto.response.PetResponse;
import competnion.domain.pet.service.PetService;
import competnion.domain.user.dto.request.RegisterPetRequest;
import competnion.global.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    /**
     * TODO : userId를 클라이언트말고 어노테이션을 만들어 securitycontextholder에서 가져오기
     */

    // 펫 등록
    @PostMapping("/register-pet/{user-id}")
    public ResponseEntity<Response> registerPet(
            @PathVariable("user-id") final Long userId,
            @Valid @RequestBody      final RegisterPetRequest registerPetRequest
    ) {
        PetResponse petResponse = petService.registerPet(userId, registerPetRequest);

        return ResponseEntity.ok(Response.success(petResponse));
    }

    // 펫 이미지 수정

    // 펫 정보 수정

    // 펫 삭제
}
