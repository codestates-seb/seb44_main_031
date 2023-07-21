package competnion.domain.community.controller;

import competnion.domain.community.dto.request.ArticleDto.ArticlePostRequest;
import competnion.domain.community.dto.request.AttendRequest;
import competnion.domain.community.dto.request.UpdateArticleRequest;
import competnion.domain.community.dto.response.ArticleResponse;
import competnion.domain.community.dto.response.WriterResponse;
import competnion.domain.community.response.MultiArticleResponse;
import competnion.domain.community.response.SingleArticleResponseDto;
import competnion.domain.community.service.CommunityService;
import competnion.domain.pet.dto.response.PetResponse;
import competnion.domain.user.annotation.UserContext;
import competnion.domain.user.entity.User;
import competnion.global.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

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

    // 게시글 수정
    @PatchMapping("/{article-id}")
    public Response<Void> updateArticle(
            @UserContext final User user,
            @Positive @PathVariable("article-id") final Long articleId,
            @Valid @RequestPart("request") final UpdateArticleRequest request,
            @RequestPart(value = "image", required = false) final List<MultipartFile> images
    ) {
        communityService.updateArticle(user, articleId, request, images);
        return Response.success();
    }

    // 게시글 참여
    @PostMapping("/attend")
    public Response<Void> attend(@UserContext final User user, @Valid @RequestBody final AttendRequest attendRequest) {
        communityService.attend(user, attendRequest);
        return Response.success();
    }

    // 게시글 참여 취소
    @DeleteMapping("/cancel/{article-id}")
    public Response<Void> cancelAttend(
            @UserContext                          final User user,
            @Positive @PathVariable("article-id") final Long articleId
    ) {
        communityService.cancelAttend(user, articleId);
        return Response.success();
    }

    // 산책 완료
    @PostMapping("/close/{article-id}")
    public Response<Void> close(
            @UserContext final User user,
            @Positive @PathVariable("article-id") final Long articleId
    ) {
        communityService.close(user, articleId);
        return Response.success();
    }

    @Scheduled(cron = "0 0/30 6-23 * * *", zone = "Asia/Seoul")
    @GetMapping("/scheduled-close")
    public void close() {
        communityService.closeScheduled();
    }

    // 게시글 상세 조회
    @GetMapping("/{article-id}")
    public ResponseEntity<SingleArticleResponseDto> getArticle(@PathVariable("article-id") @Positive Long articleId){
        return new ResponseEntity<>(communityService.findArticle(articleId), HttpStatus.OK);
    }

    /** 전체 조회 **/
    @GetMapping
    public ResponseEntity<MultiArticleResponse> getAllArticles(
            @UserContext final User user,
            @RequestParam(value = "keyword",    required = false, defaultValue = "")   final String keyword,
            @RequestParam(value = "days",       required = false, defaultValue = "30") final Integer days,
            @RequestParam(value = "page", required = false, defaultValue = "1")  final int page,
            @RequestParam(value = "size",   required = false, defaultValue = "10") final int size
    ) {
        PageRequest pageable = PageRequest.of(page - 1, size);


        return new ResponseEntity<>(communityService.getAll(user, keyword, days, pageable),HttpStatus.OK);

    }
    // 게시글 삭제
    @DeleteMapping("/{article-id}")
    public Response<Void> deleteArticle(
            @UserContext                          final User user,
            @Positive @PathVariable("article-id") final Long articleId
    ) {
        communityService.deleteArticle(user, articleId);
        return Response.success();
    }

}

