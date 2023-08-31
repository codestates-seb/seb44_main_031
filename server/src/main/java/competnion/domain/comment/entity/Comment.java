package competnion.domain.comment.entity;

import competnion.domain.community.entity.Article;
import competnion.domain.user.entity.User;
import competnion.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Table(name = "comments")
@NoArgsConstructor(access = PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long commentId;
    @Column (nullable = false, length = 200)
    private String body;
    @ManyToOne (fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne (fetch = LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Builder(builderMethodName = "createComment")
    public Comment (String body, User user, Article article) {
        this.body = body;
        this.user = user;
        this.article = article;
    }
}