package competnion.domain.comment.repository;

import competnion.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    Optional<Comment> findCommentByCommentId(long commentId);
    @Override
    void deleteById(Long commentId);
}