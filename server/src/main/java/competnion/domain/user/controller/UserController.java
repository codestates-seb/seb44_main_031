package competnion.domain.user.controller;

import competnion.domain.user.dto.request.LocationRequest;
import competnion.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register-location")
    public ResponseEntity<?> registerLocation(@Valid @RequestBody LocationRequest locationRequest) {
        return null;
    }
}
