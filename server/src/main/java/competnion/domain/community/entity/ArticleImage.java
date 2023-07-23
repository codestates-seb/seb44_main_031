package competnion.domain.community.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;
import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "article_image")
public class ArticleImage {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "article_image_id")
    private Long id;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    @OnDelete(action = CASCADE)
    private Article article;

    @Builder(builderMethodName = "saveImage")
    private ArticleImage(String imgUrl, Article article) {
        hasText(imgUrl, "imgUrl must not be empty");
        notNull(article, "article must not be empty");
        this.imgUrl = imgUrl;
        this.article = article;
    }
}
