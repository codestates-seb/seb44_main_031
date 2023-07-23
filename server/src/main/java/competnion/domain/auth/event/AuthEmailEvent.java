package competnion.domain.auth.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthEmailEvent {
    private final String email;
}
