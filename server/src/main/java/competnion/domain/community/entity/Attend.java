package competnion.domain.community.entity;

import competnion.domain.pet.entity.Pet;
import competnion.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "attend_id")
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "article_id")
    private Article article;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Builder(builderMethodName = "CreateAttend")
    private Attend(final Article article, final User user) {
        this.article = article;
        this.user = user;
    }
}
