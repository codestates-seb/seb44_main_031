package competnion.domain.community.entity;

import competnion.domain.comment.entity.Comment;
import competnion.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "articles")
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToMany(mappedBy = "article", cascade = {CascadeType.REMOVE})
    private List<Comment> comments = new ArrayList<>();
    private Duration timeDifference;

    public Article(LocalDateTime date) {
        this.date = date;
        this.timeDifference = calculateTimeDifference(date);
    }

    // 게시글의 date 필드와 현재 시간 간의 차이 계산 메소드
    private Duration calculateTimeDifference(LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();
        return Duration.between(date, now);
    }


    public Article(Long articleId, String title, String body, boolean isPassed, boolean isAttended, String location,
                        LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.articleId = articleId;
        this.title = title;
        this.body =body;
        this.isPassed = isPassed;
        this.isAttended = isAttended;
        this.location = location;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
