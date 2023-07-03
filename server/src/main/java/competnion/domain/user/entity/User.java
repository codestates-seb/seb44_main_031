package competnion.domain.user.entity;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(unique = true)
    @NotNull
    private String username;
    @Column(unique = true)
    @NotNull
    private String email;
    @NotNull
    private String password;

    private Point point;
    private String location;

    private String imgUrl;

    @OneToMany(mappedBy = "user")
    private List<Pet> pets = new ArrayList<>();

    @Builder
    private User(String username, String email, String password, Point point, String location, String imgUrl) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.point = point;
        this.location = location;
        this.imgUrl = imgUrl;
    }

    public void savePoint(Double latitude, Double longitude) throws ParseException {
        this.point =
                latitude != null && longitude != null ?
                        (Point) new WKTReader().read(String.format("POINT(%s %s)", latitude, longitude))
                        : null;

    }
}
