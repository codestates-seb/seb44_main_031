package competnion.domain.user.entity;

import com.sun.istack.NotNull;
import competnion.domain.dog.Pet;
import competnion.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
    private Long id;
    @Column(unique = true, nullable = false)
    @NotNull
    private String username;
    @Column(unique = true, nullable = false)
    @NotNull
    private String email;
    @Column(unique = true, nullable = false)
    @NotNull
    private String password;

    private Point point;
    private String address;

    private String imgUrl;

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();

    public void updateAddressAndCoordinates(String address, Point point) {
        hasText(address, "address must not be null");
        notNull(point, "point must not be null");
        this.address = address;
        this.point = point;
    }

    public void updateImgUrl(String imgUrl) {
        hasText(imgUrl, "imgUrl must not be null");
        this.imgUrl = imgUrl;
    }

    @Builder(builderClassName = "SignUp", builderMethodName = "SignUp")
    private User(final String username, final String email, final String password) {
        hasText(username, "username must not be null");
        hasText(email, "email must not be null");
        hasText(password, "password must not be null");
        this.username = username;
        this.email = email;
        this.password = password;
    }
}