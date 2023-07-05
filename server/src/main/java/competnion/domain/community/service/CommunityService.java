package competnion.domain.community.service;

import competnion.domain.community.entity.Article;
import competnion.domain.community.repository.ArticleRepository;
import competnion.domain.user.entity.User;
import competnion.global.exception.BusinessException;
import competnion.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.Optional;

@Service

public class CommunityService {

    private final ArticleRepository articleRepository;

    public CommunityService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    /**게시글 생성 메서드 **/
    public Article createArticle(Article article) {
        /**작성한 회원이 존재하는지에 대한 여부 필요 **/
        return articleRepository.save(article);
    }

    /**게시글 수정하는 메서드 **/
    public Article updateArticle(Article article) {
        Article findArticle = findVerifiedArticle(article.getArticleId());
        /**추후 게시글을 작성자만 수정할 수 있는 로직 필요 **/
        Optional.ofNullable(article.getBody()).ifPresent(body -> findArticle.setBody(body));
        Optional.ofNullable(article.getTitle()).ifPresent(title -> findArticle.setTitle(title));

        return articleRepository.save(findArticle);
    }

    /**게시글 전제 조회 메서드 **/
    public Page<Article> findArticles(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size);
        return articleRepository.findAll(pageable);
    }

    public  Article findArticle(long articleId){
        Article findArticle = findVerifiedArticle(articleId);

        articleRepository.save(findArticle);

        return findArticle;
    }
    /** 게시글이 등록된 게시글인지 확인하는 메서드 **/
    public Article findVerifiedArticle(long articleId){

        Optional<Article> findArticle = articleRepository.findById(articleId);

        Article article = findArticle.orElseThrow(() ->
                new BusinessException(ErrorCode.INVALID_INPUT_VALUE));

        return article;
    }
}
