package competnion.domain.community.entity;

import competnion.domain.pet.entity.Pet;
import competnion.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "attends")
public class Attend {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "article_id")
    private Article article;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;
    private Boolean isAttend;

    @Builder(builderMethodName = "saveAttend")
    private Attend(final Article article, final User user, final Pet pet, final Boolean isAttend) {
        this.article = article;
        this.user = user;
        this.pet = pet;
        this.isAttend = isAttend;
    }
}
