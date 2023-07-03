package competnion.domain.user.controller;

import competnion.domain.user.entity.User;
import competnion.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final UserRepository userRepository;

    @PostMapping("/test/point")
    public ResponseEntity<?> testPoint() throws ParseException {
        userRepository.save(
                User.builder()
                        .username("test")
                        .email("dlawjdals990218@naver.com")
                        .password("testPassword")
                        .point(createPoint(12.15, 10.18666))
                        .build());
        userRepository.save(
                User.builder()
                        .username("test2")
                        .email("dlawjdals9902218@naver.com")
                        .password("test2Password")
                        .point(createPoint(13.50, 11.22666))
                        .build());
        return null;
    }

    @GetMapping("/test/getpoint")
    public ResponseEntity<?> testGetPoint() throws ParseException {
        User user = userRepository.findById(1L).orElseThrow();
        System.out.println(user.getPoint());
        return null;
    }

    public Point createPoint(Double latitude, Double longitude) throws ParseException {
        return (Point)new WKTReader().read(String.format("POINT(%s %s)", longitude, latitude));
    }
}
