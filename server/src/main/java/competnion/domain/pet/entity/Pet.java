package competnion.domain.pet.entity;

import competnion.domain.community.entity.Article;
import competnion.domain.community.entity.Attend;
import competnion.domain.user.entity.User;
import competnion.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.util.Lazy;
import org.springframework.util.Assert;

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
    private String mbti;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "article_id")
    private Article article;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "breed_id")
    private Breed breed;

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

    public void updateArticle(final Article article) {
        notNull(article, "article must not be null");
        this.article = article;
    }

    @Builder(builderClassName = "RegisterPet", builderMethodName = "RegisterPet")
    private Pet(final String name, final LocalDate birth ,final Boolean gender, final Boolean neutralization, final String imgUrl, final User user, final String mbti, final Breed breed) {
        hasText(name, "name must not be empty");
        notNull(birth, "birth must not be null");
        isInstanceOf(Boolean.class, gender, "Boolean expected");
        isInstanceOf(Boolean.class, neutralization, "Boolean expected");
        notNull(user, "user must not be null");
        hasText(mbti, "mbti must not be empty");
        notNull(breed, "breed must not be null");
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.neutralization = neutralization;
        this.imgUrl = imgUrl;
        this.user = user;
        this.mbti = mbti;
        this.breed = breed;
    }
}
