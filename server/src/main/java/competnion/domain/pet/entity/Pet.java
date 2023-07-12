package competnion.domain.pet.entity;

import competnion.domain.community.entity.Article;
import competnion.domain.user.entity.User;
import competnion.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.util.Lazy;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @NotNull
    private LocalDate birth;
    @Column(nullable = false)
    @NotNull
    private Boolean gender;
    @Column(nullable = false)
    @NotNull
    private Boolean neutralization;
    @Column(nullable = false)
    @NotBlank
    private String imgUrl;
    @Column(nullable = false)
    @NotBlank
    private String vaccine;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void updateImgUrl(final String imgUrl) {
        hasText(imgUrl, "imgUrl must not be null");
        this.imgUrl = imgUrl;
    }

    public void updateName(final String name) {
        hasText(name, "'name' must not be empty");
        this.name = name;
    }

    public void updateBirth(final LocalDate birth) {
        notNull(birth, "birth must not be null");
        this.birth = birth;
    }

    public void updateNeutralization(final Boolean neutralization) {
        isInstanceOf(Boolean.class, neutralization, "Boolean expected");
        this.neutralization = neutralization;
    }

    public void updateVaccine(final String vaccine) {
        hasText(vaccine, "vaccine must not be empty");
        this.vaccine = vaccine;
    }

    @Builder(builderClassName = "RegisterPet", builderMethodName = "RegisterPet")
    private Pet(final String name, final LocalDate birth ,final Boolean gender, final Boolean neutralization, final String imgUrl, final String vaccine, final User user) {
        hasText(name, "name must not be null");
        notNull(birth, "birth must not be null");
        isInstanceOf(Boolean.class, gender, "Boolean expected");
        isInstanceOf(Boolean.class, neutralization, "Boolean expected");
        hasText(imgUrl, "imgUrl must not be null");
        hasText(vaccine, "inoculated must not be null");
        notNull(user, "user must not be null");
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.neutralization = neutralization;
        this.imgUrl = imgUrl;
        this.vaccine = vaccine;
        this.user = user;
    }
}
