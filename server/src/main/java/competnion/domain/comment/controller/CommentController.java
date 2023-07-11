//package competnion.domain.comment.controller;
//
//import competnion.domain.comment.dto.CommentDto;
//import competnion.domain.comment.entity.Comment;
//import competnion.domain.comment.mapper.CommentMapper;
//import competnion.domain.comment.service.CommentService;
//import competnion.global.security.interceptor.JwtParseInterceptor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.constraints.Positive;
//
//@Slf4j
//@RestController
//@RequestMapping("/articles/{article-id}/comments")
//public class CommentController {
//
//    private final CommentService commentService;
//
//    private final CommentMapper mapper;
//
//    // request의 AccessToken에서 memberId 추출;
//    private static long authenticatedId = JwtParseInterceptor.getAuthenticatedUserId();
//
//    public CommentController(CommentService commentService, CommentMapper mapper) {
//        this.commentService = commentService;
//        this.mapper = mapper;
//    }
//
//    @PostMapping
//    public ResponseEntity postComment(@RequestBody CommentDto.Post postDto,
//                                      @PathVariable("article-id") long articleId) {
//
//        log.info("CommentController - postComment 실행");
//
//        postDto.setArticleId(articleId);
//
//        Comment comment = mapper.commentPostToComment(postDto);
//        Comment createdComment = commentService.createComment(comment,authenticatedId);
//        CommentDto.Response response = mapper.commentToCommentResponse(createdComment);
//
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }
//
//    @PatchMapping("/{comment-id}")
//    public ResponseEntity patchComment(
//                                        @Positive @PathVariable("comment-id") long commentId,
//                                        @RequestBody CommentDto.Patch patchDto) {
//
//        log.info("CommentController - patchComment 실행");
//
//        Comment comment = mapper.commentPatchToComment(patchDto);
//        Comment updatedComment = commentService.updateComment(comment,authenticatedId);
//        CommentDto.Response response = mapper.commentToCommentResponse(updatedComment);
//
//        return new ResponseEntity<>(response,HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{comment-id}")
//    public ResponseEntity deleteComment(@Positive @PathVariable("comment-id") long commentId) {
//
//        log.info("CommentController - deleteComment 실행");
//
//        commentService.deleteComment(commentId,authenticatedId);
//
//        return ResponseEntity.noContent().build();
//    }
//
//}
