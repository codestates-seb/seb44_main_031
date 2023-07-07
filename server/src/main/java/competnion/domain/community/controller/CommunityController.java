package competnion.domain.community.controller;

import competnion.domain.community.dto.request.ArticleDto;
import competnion.domain.community.dto.response.ArticleResponseDto;
import competnion.domain.community.entity.Article;
import competnion.domain.community.repository.ArticleRepository;
import competnion.domain.community.response.MultiResponseDto;
import competnion.domain.community.response.SingleResponseDto;
import competnion.domain.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@Validated
@RequestMapping("/articles")
@Slf4j
@RequiredArgsConstructor

public class CommunityController {
    private final CommunityService communityService;

    private final ArticleRepository articleRepository;

    /** 게시글 작성 **/
    @PostMapping
    public ResponseEntity createArticle(@Valid @RequestBody ArticleDto.ArticlePostDto articlePostDto) {
        /**
         * User의 애완견 정보가 없을 경우 불가능하게 예외처리.
         */
        Article createdArticle = communityService.createArticle(articlePostDto.toEntity());
        return new ResponseEntity<>(
                new SingleResponseDto<>(new ArticleResponseDto(createdArticle)), HttpStatus.CREATED);
    }

    /** 게시글 수정 **/
    @PatchMapping("/{article-id}")
    public ResponseEntity updateArticle(@PathVariable("article-id") Long articleId,
                              @Valid@RequestBody ArticleDto.ArticlePatchDto articlePatchDto) {
        communityService.updateArticle(articleId, articlePatchDto.toEntity());
        Article updatedArticle = communityService.findArticleById(articleId);
        return new ResponseEntity<>(
                new SingleResponseDto<>(new ArticleResponseDto(updatedArticle)), HttpStatus.OK);
    }
    /** 게시글 조회 **/
    @GetMapping("/{article-id}")
    public ArticleResponseDto getArticle(@PathVariable("article-id") @Positive Long articleId){
        return communityService.findById(articleId);
    }

    /** 전체 조회 **/
    @GetMapping
    public List<Article> getAllArticlesWithin3Km(@RequestParam("point") Point point) {
        /**로그인한 사용자의 위치정보를 알기 위해서는 security정보 필요, 당장은 임의로 넣음 */
        // 사용자의 위치로부터 위도와 경도 추출
        double latitude = point.getY();
        double longitude = point.getX();

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Article> page = articleRepository.findNearbyArticles(point, pageable);

        return page.getContent();
    }

    /** 질문 삭제 **/
    @DeleteMapping("/{article-id}")
    public ResponseEntity deleteArticle(@PathVariable("article-id") @Positive long articleId){
        // TODO : 삭제하려는 게시글이 작성자인지 확인하는 로직 필요
        communityService.deleteArticle(articleId);
        return ResponseEntity.noContent().build();
    }
}

