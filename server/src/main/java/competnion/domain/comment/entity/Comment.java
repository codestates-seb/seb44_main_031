package competnion.domain.comment.entity;

import competnion.domain.community.entity.Article;
import competnion.domain.user.entity.User;
import competnion.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "comments")
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;

    @Column (nullable = false, length = 200)
    private String body;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private User user;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "ARTICLE_ID")
    private Article article;



    @Builder(builderClassName = "createComment",builderMethodName = "createComment")
    public Comment (String body, User user, Article article) {
        this.body=body;
        this.user=user;
        this.article=article;
    }








}