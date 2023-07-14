package competnion.domain.comment.entity;

import competnion.domain.community.entity.Article;
import competnion.domain.user.entity.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;

    @Column (nullable = false, length = 200)
    private String body;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime modifiedAt;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID")
    private Article article;






}
