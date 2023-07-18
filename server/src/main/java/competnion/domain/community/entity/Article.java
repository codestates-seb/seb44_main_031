package competnion.domain.community.entity;

import competnion.domain.comment.entity.Comment;
import competnion.domain.pet.entity.Pet;
import competnion.domain.user.entity.User;
import competnion.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static competnion.domain.community.entity.ArticleStatus.OPEN;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
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
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "article")
    private List<ArticleImage> images = new ArrayList<>();
    @OneToMany(mappedBy = "article")
    private List<Pet> pets = new ArrayList<>();

//    @OnDelete(action= OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "article", cascade = {CascadeType.REMOVE})
    private List<Comment> comments = new ArrayList<>();
//    private Duration timeDifference;
//
//    public Article(LocalDateTime date) {
//        this.date = date;
//        this.timeDifference = calculateTimeDifference(date);
//    }
//
//    // 게시글의 date 필드와 현재 시간 간의 차이 계산 메소드
//    private Duration calculateTimeDifference(LocalDateTime date) {
//        LocalDateTime now = LocalDateTime.now();
//        return Duration.between(date, now);
//    }

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

    public void updateStatus(ArticleStatus articleStatus) {
        this.articleStatus = articleStatus;
    }

    /** 게시글 수정 */
    public void updateInfo(Article updatedArticle) {
        if (updatedArticle.getTitle() != null) {
            this.title = updatedArticle.getTitle();
        }
        if (updatedArticle.getBody() != null) {
            this.body = updatedArticle.getBody();
        }
        if (updatedArticle.getLocation() != null) {
            this.location = updatedArticle.getLocation();
        }
        if (updatedArticle.getStartDate() != null) {
            this.startDate = updatedArticle.getStartDate();
        }
        if (updatedArticle.getAttendant() != 0) {
            this.attendant = updatedArticle.getAttendant();
        }
    }

    public void attend() {
        this.attendant--;
    }
}
