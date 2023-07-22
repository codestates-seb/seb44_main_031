package competnion.domain.user.entity;

import competnion.domain.community.entity.Article;
import competnion.domain.community.entity.Attend;
import competnion.domain.pet.entity.Pet;
import competnion.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

@Getter
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
    private String nickname;
    @Column(unique = true, nullable = false)
    @NotBlank
    private String email;
    @Column(unique = true, nullable = false)
    @NotBlank
    private String password;
    @NotNull
    private Point point;
    private Double latitude;
    private Double longitude;
    @NotBlank
    private String address;
    private String imgUrl;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // 태영 추가 (유저 권한)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Article> articles = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Attend> attends = new ArrayList<>();

    public void updateAddressAndCoordinates(final String address, final Point point, final Double latitude, final Double longitude) {
        hasText(address, "address must not be empty");
        notNull(point, "point must not be null");

        this.address = address;
        this.point = point;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void updateImgUrl(final String imgUrl) {
        hasText(imgUrl, "imgUrl must not be empty");
        this.imgUrl = imgUrl;
    }

    public void updateNickname(final String nickname) {
        hasText(nickname, "nickname must not be empty");
        this.nickname = nickname;
    }

    public void updatePassword(final String password) {
        hasText(password, "password must not be empty");
        this.password = password;
    }

    public void addPet(final Pet pet) {
        notNull(pet, "pet must not be null");
        this.pets.add(pet);
    }

    public void idToDetails(final Long id) {
        notNull(id, "id must not be null");
        this.id = id;
    }

    public void emailToDetails(final String email) {
        hasText(email, "nickname must not be empty");
        this.email = email;
    }

    public void passwordToDetails(final String password) {
        hasText(password, "password must not be empty");
        this.password = password;
    }

    public void rolesToDetails(final List<String> roles) {
        notNull(roles, "roles must not be null");
        this.roles = roles;
    }

    @Builder(builderMethodName = "signUp")
    private User(final String nickname, final String email, final String password, final String address, final Point point, final List<String> roles, final Double latitude, final Double longitude, final String imgUrl) {
        hasText(nickname, "nickname must not be empty");
        hasText(email, "email must not be empty");
        hasText(password, "password must not be empty");
        hasText(address, "address must not be empty");
        notNull(point, "point must not be null");
        hasText(imgUrl, "imgUrl must not be empty");
        notNull(roles, "roles must not be null");
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.address = address;
        this.point = point;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imgUrl = imgUrl;
        this.roles = roles;
    }
}
