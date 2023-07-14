package competnion.domain.comment.mapper;

import competnion.domain.comment.dto.CommentDto;
import competnion.domain.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    Comment commentPostToComment(CommentDto.Post commentPostDto);
    CommentDto.Response commentToCommentResponse(Comment comment);

    default List<CommentDto.Response> commentsToCommentResponses(List<Comment> comments) {
        return comments.stream()
                .map(comment -> commentToCommentResponse(comment))
                .collect(Collectors.toList());
    }

}