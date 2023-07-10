//package competnion.domain.comment.service;
//
//import competnion.domain.comment.entity.Comment;
//import competnion.domain.comment.repository.CommentRepository;
//import competnion.global.exception.BusinessLogicException;
//import competnion.global.exception.ExceptionCode;
//import competnion.global.security.interceptor.JwtParseInterceptor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//@Transactional
//@Service
//@Slf4j
//public class CommentService {
//
//    private final CommentRepository commentRepository;
//
//    private final ArticleRepository articleRepository;
//
//    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository) {
//        this.commentRepository = commentRepository;
//        this.articleRepository = articleRepository;
//    }
//
//    public Comment createComment(Comment comment, long memberId) {
//
//        long memberIdInToken = JwtParseInterceptor.getAuthenticatedMemberId();
//
//        comment.getMember().setMemberId(memberIdInToken);
//
//        Article article = articleRepository.findByMemberId(memberId);
//
//        Comment savedComment =  commentRepository.save(comment);
//
//        return savedComment;
//    }
//
//    public Comment updateComment(Comment comment,long authenticatedId) {
//
//        checkCommentOwner(comment.getMember().getMemberId(),authenticatedId);
//
//
//        // DB에서 수정된 comment의 commentId가 일치하는 comment 찾기
//        Comment findComment = commentRepository.findCommentByCommentId(comment.getCommentId());
//
//        // 찾은 comment 내용을 수정된 내용으로 바꿔주기
//        Optional.ofNullable(comment.getBody())
//                .ifPresent(body -> findComment.setBody(body));
//
//        // 수정된 comment db 저장
//        Comment updatedComment = commentRepository.save(findComment);
//
//        return updatedComment;
//    }
//
//    public void deleteComment(long commentId,long authenticatedId) {
//
//        Comment findComment = commentRepository.findCommentByCommentId(commentId);
//
//        checkCommentOwner(findComment.getMember().getMemberId(), authenticatedId);
//
//        commentRepository.deleteById(commentId);
//
//    }
//
//    public void checkCommentOwner(long ownerId,long authenticatedId) {
//
//        // 댓글에 등록된 memberId와 request의 memberId가 같은지 판별
//        if (!(ownerId == authenticatedId)) {
//            throw new BusinessLogicException(ExceptionCode.ACCESS_NOT_ALLOWED);
//        }
//    }
//
//}
