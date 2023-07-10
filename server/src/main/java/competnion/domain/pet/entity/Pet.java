package competnion.domain.pet.entity;

import competnion.domain.user.entity.User;
import competnion.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.Assert.*;

@Getter
@Entity
@DynamicUpdate
@Table(name = "pets")
@NoArgsConstructor(access = PROTECTED)
public class Pet extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "pet_id", nullable = false, insertable = false, updatable = false)
    private Long id;
    @Column(nullable = false)
    @NotBlank
    private String name;
    @Column(nullable = false)
//    @NotBlank
    private LocalDateTime birth;
    @Column(nullable = false)
    @NotNull
    private Boolean gender;
    @Column(nullable = false)
    @NotNull
    private Boolean isNeutered;
    @Column(nullable = false)
    @NotBlank
    private String imgUrl;
    @Column(nullable = false)
    @NotBlank
    private String inoculated;
    private Boolean isMain;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void updateImgUrl(final String imgUrl) {
        hasText(imgUrl, "imgUrl must not be null");
        this.imgUrl = imgUrl;
    }

    @Builder(builderClassName = "RegisterPet", builderMethodName = "RegisterPet")
    private Pet(final String name, final LocalDateTime birth, final Boolean gender, final Boolean isNeutered, final String imgUrl, final String inoculated, final User user) {
        hasText(name, "name must not be null");
//        notNull(birth, "birth must not be null");
        isInstanceOf(Boolean.class, gender, "Boolean expected");
        isInstanceOf(Boolean.class, isNeutered, "Boolean expected");
        hasText(imgUrl, "imgUrl must not be null");
        hasText(inoculated, "inoculated must not be null");
        notNull(user, "user must not be null");
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.isNeutered = isNeutered;
        this.imgUrl = imgUrl;
        this.inoculated = inoculated;
        this.user = user;
    }
}
