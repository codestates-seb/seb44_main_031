package competnion.domain.user.entity;

import competnion.domain.pet.entity.Pet;
import competnion.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Entity
@DynamicInsert
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
    private Point point;
    private String address;
    private String imgUrl;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

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

    public void addPet(Pet pet) {
        this.pets.add(pet);
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
