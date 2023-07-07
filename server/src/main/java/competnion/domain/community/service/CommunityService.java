package competnion.domain.community.service;

import competnion.domain.community.dto.response.ArticleResponseDto;
import competnion.domain.community.entity.Article;
import competnion.domain.community.repository.ArticleRepository;
import competnion.domain.user.entity.User;
import competnion.global.exception.BusinessException;
import competnion.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final ArticleRepository articleRepository;

    @Transactional
    /**게시글 생성 메서드 **/
    public Article createArticle(Article article) {
        /**작성한 회원이 존재하는지에 대한 여부 필요 **/
        return articleRepository.save(article);
    }

    @Transactional
    /**게시글 수정 메서드 **/
    public void updateArticle(Long articleId,Article article) {
        Article findArticle = findArticleById(articleId);
        /**추후 게시글을 작성자만 수정할 수 있는 로직 필요 **/
        findArticle.update(article);
    }

    /**게시글 조회 메서드 **/
    public ArticleResponseDto findById(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        return new ArticleResponseDto(article);
    }

    @Transactional(readOnly = true)
    /**게시글 전제 조회 메서드 **/
    public Page<Article> findNearbyArticles(int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size);
        //추후 로그인한 유저의 point값으로 거리계산해야함. 임시로 findall
        return articleRepository.findAll(pageable);
    }

    /** 질문 삭제 메서드 **/
    public void deleteArticle(long articleId){

        Article findArticle = findVerifiedArticle(articleId);
        //TODO : 작성자 여부 확인 필요
        articleRepository.delete(findArticle);
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
    /** 게시글 찾는 메서드 **/
    public Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));
    }
}
