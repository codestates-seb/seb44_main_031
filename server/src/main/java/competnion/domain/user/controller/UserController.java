package competnion.domain.user.controller;

import competnion.domain.user.dto.request.AddressRequest;
import competnion.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register-location")
    public ResponseEntity<?> editAddressAndCoordinates(
            @PathVariable("user-id") final Long userId,
            @Valid @RequestBody      final AddressRequest addressRequest
    ) {
        userService.editAddress(userId, addressRequest);
        return ResponseEntity.ok().build();
    }
}
