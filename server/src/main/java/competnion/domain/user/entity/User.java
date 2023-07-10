package competnion.domain.user.entity;

import competnion.domain.pet.entity.Pet;
import competnion.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "users")
@NoArgsConstructor(access = PROTECTED)
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() where user_id = ?")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
    private Long id;
    @Column(unique = true, nullable = false)
    @NotBlank
    private String username;
    @Column(unique = true, nullable = false)
    @NotBlank
    private String email;
    @Column(unique = true, nullable = false)
    @NotBlank
    private String password;
    @Lob
    @Setter
    private Point point;
    @Setter
    private String address;
    private String imgUrl;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();

    // 태영 추가 (유저 권한)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    public void updateAddressAndCoordinates(final String address, final Point point) {
        hasText(address, "address must not be empty");
        notNull(point, "point must not be null");
        this.address = address;
        this.point = point;
    }

    public void updateImgUrl(final String imgUrl) {
        hasText(imgUrl, "imgUrl must not be empty");
        this.imgUrl = imgUrl;
    }

    public void updateUsername(final String username) {
        hasText(username, "username must not be empty");
        this.username = username;
    }

    public void addPet(Pet pet) {
        notNull(pet, "pet must not be null");
        this.pets.add(pet);
    }

    @Builder(builderClassName = "SignUp", builderMethodName = "SignUp")
    private User(final String username, final String email, final String password, final String address, final Point point) {
        hasText(username, "username must not be empty");
        hasText(email, "email must not be empty");
        hasText(password, "password must not be empty");
        hasText(address, "address must not be empty");
        notNull(point, "point must not be null");
        this.username = username;
        this.email = email;
        this.password = password;
        this.address = address;
        this.point = point;
    }

    // 태영 추가
    @Builder(builderMethodName = "UserDetails")
    private User(final Long id, final String email, final String password, final List<String> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

}
