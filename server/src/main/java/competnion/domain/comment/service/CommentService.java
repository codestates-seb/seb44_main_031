package competnion.domain.comment.service;

import competnion.domain.comment.dto.CommentDto;
import competnion.domain.comment.entity.Comment;
import competnion.domain.comment.mapper.CommentMapper;
import competnion.domain.comment.repository.CommentRepository;
import competnion.domain.community.repository.ArticleRepository;
import competnion.domain.user.repository.UserRepository;
import competnion.global.exception.BusinessLogicException;
import competnion.global.exception.ExceptionCode;
import competnion.global.security.interceptor.JwtParseInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper mapper;
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;



    public void createComment(CommentDto.Post post, long articleId) {

        long authenticatedId = JwtParseInterceptor.getAuthenticatedUserId();

        Comment comment = Comment.createComment()
                .body(post.getBody())
                .user(userRepository.findById(authenticatedId)
                        .orElseThrow(()->new BusinessLogicException(ExceptionCode.USER_NOT_FOUND)))
                .article(articleRepository.findArticleById(articleId))
                .build();


        commentRepository.save(comment);

    }

    public void deleteComment(long commentId) {

        if (!checkWriter(commentId)) {throw new BusinessLogicException(ExceptionCode.ACCESS_NOT_ALLOWED);}

        commentRepository.deleteById(commentId);
    }

    public Boolean checkExistComment(long commentId) {
        return commentRepository.findCommentByCommentId(commentId).isPresent();
    }

    public Boolean checkWriter(long commentId) {

        Comment findComment = commentRepository.findCommentByCommentId(commentId)
                .orElseThrow(()->new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));

        long writerUserId = findComment.getUser().getId();

        long requestUserId = JwtParseInterceptor.getAuthenticatedUserId();

        return writerUserId == requestUserId;
    }



}