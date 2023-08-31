package competnion.domain.community.controller;

import competnion.domain.community.dto.request.ArticleRequestDto.ArticlePostRequest;
import competnion.domain.community.dto.request.ArticleRequestDto.UpdateArticleRequest;
import competnion.domain.community.dto.request.AttendRequest;
import competnion.domain.community.dto.response.WriterResponse;
import competnion.domain.community.response.MultiArticleResponse;
import competnion.domain.community.response.SingleArticleResponseDto;
import competnion.domain.community.service.CommunityService;
import competnion.domain.pet.dto.response.PetResponse;
import competnion.domain.user.entity.User;
import competnion.global.common.annotation.AuthenticatedUser;
import competnion.global.response.Response;
import competnion.infra.redis.lock.AttendLockFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class CommunityController {
    private final CommunityService communityService;
    private final AttendLockFacade attendLockFacade;

    @GetMapping("/writer-info")
    public Response<WriterResponse> getWriterInfo(@AuthenticatedUser final User user) {
        return Response.success(communityService.getWriterInfo(user));
    }

    @GetMapping("/attendee-info/{article-id}")
    public Response<List<PetResponse>> getAttendeeInfo(
            @AuthenticatedUser                    final User user,
            @Positive @PathVariable("article-id") final Long articleId
    ) {
        return Response.success(communityService.getAttendeePetInfo(user, articleId));
    }

    /** 게시글 작성 **/
    @PostMapping
    public Response<Long> createArticle(
            @AuthenticatedUser                              final User user,
            @Valid @RequestPart("request")                  final ArticlePostRequest articlePostDto,
            @RequestPart(value = "image", required = false) final List<MultipartFile> images
    ) {
        return Response.success(communityService.createArticle(user, articlePostDto, images));
    }

    // 게시글 수정
    @PatchMapping("/{article-id}")
    public Response<Void> updateArticle(
            @AuthenticatedUser                              final User user,
            @Valid @RequestPart("request")                  final UpdateArticleRequest request,
            @Positive @PathVariable("article-id")           final Long articleId,
            @RequestPart(value = "image", required = false) final List<MultipartFile> images
    ) {
        communityService.updateArticle(user, articleId, request, images);
        return Response.success();
    }

    // 게시글 참여
    @PostMapping("/{article-id}/attend")
    public Response<Void> attendArticle(
            @AuthenticatedUser                    final User user,
            @Valid @RequestBody                   final AttendRequest request,
            @Positive @PathVariable("article-id") final Long articleId
    ) {
        attendLockFacade.attend(user, request, articleId);
        return Response.success();
    }

    // 게시글 참여 취소
    @DeleteMapping("/{article-id}/cancel/attend")
    public Response<Void> cancelAttend(
            @AuthenticatedUser                    final User user,
            @Positive @PathVariable("article-id") final Long articleId
    ) {
        communityService.cancelAttend(user, articleId);
        return Response.success();
    }

    // 산책 완료
    @PostMapping("/close/{article-id}")
    public Response<Void> closeMeeting(
            @AuthenticatedUser                    final User user,
            @Positive @PathVariable("article-id") final Long articleId
    ) {
        communityService.closeArticle(user, articleId);
        return Response.success();
    }

    // 게시글 상세 조회
    @GetMapping("/{article-id}")
    public ResponseEntity<SingleArticleResponseDto> getArticle(@PathVariable("article-id") @Positive Long articleId){
        return new ResponseEntity<>(communityService.findArticle(articleId), OK);
    }

    /** 전체 조회 **/
    @GetMapping
    public ResponseEntity<MultiArticleResponse> getAllArticles(
            @AuthenticatedUser                                                    final User user,
            @RequestParam(value = "days")                                         final Integer days,
            @RequestParam(value = "sort")                                         final String sort,
            @RequestParam(value = "page")                                         final int page,
            @RequestParam(value = "size")                                         final int size,
            @RequestParam(value = "keyword", required = false, defaultValue = "") final String keyword
    ) {
        PageRequest pageable = sort.equals("asc")
                                ? PageRequest.of(page - 1, size, Sort.by("startDate").ascending())
                                : PageRequest.of(page - 1, size, Sort.by("startDate").descending());
        return new ResponseEntity<>(communityService.getAll(user, keyword, days, pageable), HttpStatus.OK);
    }

    // 게시글 삭제
    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{article-id}")
    public Response<Void> deleteArticle(
            @AuthenticatedUser                    final User user,
            @Positive @PathVariable("article-id") final Long articleId
    ) {
        communityService.deleteArticle(user, articleId);
        return Response.success();
    }

}

