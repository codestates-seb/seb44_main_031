package competnion.domain.community.entity;

import competnion.domain.comment.entity.Comment;
import competnion.domain.user.entity.User;
import lombok.*;
import net.bytebuddy.asm.Advice;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long articleId;
    @Column(nullable = false)

    private String title;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false) // 시간이 지났는지 여부
    private boolean isPassed;

    @Column(nullable = false) // 참석자가 있는지 여부
    private boolean isAttended;

    @Column(nullable = false)
    private String location;

    private Point point;

    @Column(nullable = false)
    private LocalDateTime date;

    /**
     * limit은 예약어라 불가피하게 변경
     */
    @Column(nullable = false)
    private int attendant;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @OnDelete(action= OnDeleteAction.CASCADE)
//    @OneToMany(mappedBy = "article", cascade = {CascadeType.REMOVE})
//    private List<Comment> comments = new ArrayList<>();
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

    @Builder
    private Article(User user, String title, String body, String location, int attendant, LocalDateTime date) {
        this.user = user;
        this.title = title;
        this.body = body;
        this.isPassed = false;
        this.isAttended = false;
        this.attendant = attendant;
        this.location = location;
        this.date = date;
    }

    public static Article of(User user, Article article) {
        return Article.builder()
                .user(user)
                .title(article.getTitle())
                .body(article.getBody())
                .attendant(article.getAttendant())
                .date(article.getDate())
                .location(article.getLocation())
                .build();
    }


    /** 게시글 수정 */
    public void update(Article article) {
        this.title = article.title;
        this.body = article.body;
        this.location = article.location;
        this.date = article.date;
        this.attendant = article.attendant;
    }

    public void attend() {
        this.attendant--;
    }
}
