package competnion.domain.community.controller;

import competnion.domain.community.dto.request.ArticleDto;
import competnion.domain.community.dto.response.ArticleResponseDto;
import competnion.domain.community.entity.Article;
import competnion.domain.community.mapper.ArticleMapper;
import competnion.domain.community.repository.ArticleRepository;
import competnion.domain.community.response.MultiResponseDto;
import competnion.domain.community.response.SingleResponseDto;
import competnion.domain.community.service.CommunityService;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;

@RestController
@Validated
@RequestMapping("/articles")
@Slf4j

public class CommunityController {
    private final CommunityService communityService;

    private final ArticleMapper articleMapper;

    public CommunityController(CommunityService communityService, ArticleMapper articleMapper) {
        this.communityService = communityService;
        this.articleMapper = articleMapper;
    }


    /** 게시글 작성 **/
    @PostMapping
    public ResponseEntity postArticle(@Valid @RequestBody ArticleDto.ArticlePostDto articlePostDto){
        /**
         * User의 애완견 정보가 없을 경우 불가능하게 예외처리.
         */
        Article article = communityService.createArticle(articleMapper.articlePostDtoToArticle(articlePostDto));

        return new ResponseEntity<>(
                new SingleResponseDto<>(articleMapper.articleToArticleResponseDto(article)), HttpStatus.CREATED);
    }
    /** 게시글 조회 **/
    @GetMapping("/{article-id}")
    public ResponseEntity getArticle(@PathVariable("article-id") @Positive long articleId){
        Article article = communityService.findArticle(articleId);
        return new ResponseEntity<>(new SingleResponseDto<>(
                articleMapper.articleToArticleResponseDto(article)), HttpStatus.OK);
    }
    /** 전체 조회 **/
    @GetMapping
    public ResponseEntity<?> getArticles(@RequestParam("page") int page,
                                         @RequestParam("size") int size,
                                         @RequestParam("sort") String sort,
                                         @AuthenticationPrincipal UserPrincipal userPrincipal) {
        /**로그인한 사용자의 위치정보를 알기 위해서는 security정보 필요, 당장은 임의로 넣음 */
        //Point userPoint = userPrincipal.getPoint();
        Page<Article> articlePages = communityService.findArticles(page-1,size,sort);
        List<Article> articles = articlePages.getContent();

        List<ArticleResponseDto> articleResponseDtos = articleMapper.articlesToArticleResponseDtos(articles);
        MultiResponseDto<ArticleResponseDto> responseDto = new MultiResponseDto<>(articleResponseDtos, articlePages);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}

