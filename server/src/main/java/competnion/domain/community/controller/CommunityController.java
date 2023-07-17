package competnion.domain.community.controller;

import competnion.domain.community.dto.request.ArticleDto.ArticlePostRequest;
import competnion.domain.community.dto.request.AttendRequest;
import competnion.domain.community.dto.response.ArticleResponse;
import competnion.domain.community.dto.response.ArticleResponseDto;
import competnion.domain.community.dto.response.WriterResponse;
import competnion.domain.community.entity.Article;
import competnion.domain.community.response.SingleArticleResponseDto;
import competnion.domain.community.service.CommunityService;
import competnion.domain.pet.dto.response.PetResponse;
import competnion.domain.user.annotation.UserContext;
import competnion.domain.user.entity.User;
import competnion.global.response.Response;
import competnion.global.security.interceptor.JwtParseInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class CommunityController {
    private final CommunityService communityService;

    @GetMapping("/writer-info")
    public Response<WriterResponse> getWriterInfo(@UserContext final User user) {
        return Response.success(communityService.getWriterInfo(user));
    }

    @GetMapping("/attendee-info/{article-id}")
    public Response<List<PetResponse>> getAttendeeInfo(
            @UserContext                          final User user,
            @Positive @PathVariable("article-id") final Long articleId
    ) {
        return Response.success(communityService.getAttendeePetInfo(user, articleId));
    }

    /** 게시글 작성 **/
    @PostMapping
    public Response<Long> createArticle(
            @UserContext                                    final User user,
            @Valid @RequestPart("request")                  final ArticlePostRequest articlePostDto,
            @RequestPart(value = "image", required = false) final List<MultipartFile> images
    ) {
        return Response.success(communityService.createArticle(user, articlePostDto, images));
    }

    // 게시글 참여
    @PostMapping("/attend")
    public Response<?> attend(@UserContext final User user, @Valid @RequestBody final AttendRequest attendRequest) {
        communityService.attend(user, attendRequest);
        return Response.success();
    }

    // 게시글 참여 취소
    @DeleteMapping("/cancel/{article-id}")
    public Response<?> cancelAttend(
            @UserContext                          final User user,
            @Positive @PathVariable("article-id") final Long articleId
    ) {
        communityService.cancelAttend(user, articleId);
        return Response.success();
    }

    // 게시글 상세 조회
    @GetMapping("/{article-id}")
    public ResponseEntity<SingleArticleResponseDto> getArticle(@PathVariable("article-id") @Positive Long articleId){
        return new ResponseEntity<>(communityService.findArticle(articleId), HttpStatus.OK);
    }

    /** 전체 조회 **/
    @GetMapping("/all")
    public Response<List<ArticleResponse>> getAllArticles(
            @UserContext final User user,
            @RequestParam(value = "keyword",    required = false, defaultValue = "")   final String keyword,
            @RequestParam(value = "days",       required = false, defaultValue = "30") final int days,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "1")  final int pageNumber,
            @RequestParam(value = "pageSize",   required = false, defaultValue = "10") final int pageSize
    ) {
        PageRequest pageable = PageRequest.of(pageNumber - 1, pageSize);
        List<ArticleResponse> articles = communityService.getAll(user, keyword, days, pageable);
        return Response.success(articles);
    }




    /** 질문 삭제 **/
//    @DeleteMapping("/{article-id}")
//    public ResponseEntity deleteArticle(@PathVariable("article-id") @Positive long articleId){
//        /** 삭제하려는 게시글이 작성자인지 확인하는 로직 필요(해결) */
//        Long userId = JwtParseInterceptor.getAuthenticatedUserId();
//        communityService.deleteArticle(articleId, userId);
//        return ResponseEntity.noContent().build();
//    }
}

