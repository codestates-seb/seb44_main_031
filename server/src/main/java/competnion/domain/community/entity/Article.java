package competnion.domain.community.entity;

import competnion.domain.comment.entity.Comment;
import competnion.domain.pet.entity.Pet;
import competnion.domain.user.entity.User;
import competnion.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static competnion.domain.community.entity.ArticleStatus.OPEN;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "articles")
public class Article extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "article_id")
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String body;
    @Column(nullable = false)
    private String location;
    @NotNull
    private Point point;
    @Column(nullable = false)
    private LocalDateTime startDate;
    @Column(nullable = false)
    private LocalDateTime endDate;
    @Column(nullable = false)
    private int attendant;
    @Enumerated(STRING)
    private ArticleStatus articleStatus;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "article")
    private List<ArticleImage> images = new ArrayList<>();
    @OneToMany(mappedBy = "article")
    private List<Pet> pets = new ArrayList<>();
    @OneToMany(mappedBy = "article", cascade = REMOVE)
    private List<Comment> comments = new ArrayList<>();
    @OneToMany(mappedBy = "article")
    private List<Attend> attends = new ArrayList<>();

    @Builder(builderMethodName = "createArticle")
    private Article(User user, String title, String body, String location, int attendant, LocalDateTime startDate, LocalDateTime endDate, Point point) {
        this.user = user;
        this.title = title;
        this.body = body;
        this.attendant = attendant;
        this.location = location;
        this.point = point;
        this.startDate = startDate;
        this.endDate = endDate;
        this.articleStatus = OPEN;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateBody(String body) {
        this.body = body;
    }

    public void updateStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void updateEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void updateStatus(ArticleStatus articleStatus) {
        this.articleStatus = articleStatus;
    }
}
