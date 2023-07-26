package competnion.domain.comment.controller;

import competnion.domain.comment.dto.CommentDto;
import competnion.domain.comment.service.CommentService;
import competnion.global.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/articles/{article-id}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public Response<Void> postComment(@RequestBody CommentDto.Post postDto,
                                      @PathVariable("article-id") long articleId) {

        commentService.createComment(postDto,articleId);

        return Response.success();
    }
    @DeleteMapping("/{comment-id}")
    public Response<Void> deleteComment(@Positive @PathVariable("comment-id") long commentId) {

        commentService.deleteComment(commentId);

        return Response.success();
    }
}