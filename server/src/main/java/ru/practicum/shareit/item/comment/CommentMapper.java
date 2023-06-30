package ru.practicum.shareit.item.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.Generated;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CommentMapper {
    public Comment mapToComment(CommentRequest commentRequest, User user, Item item, LocalDateTime now) {
        Comment comment = new Comment();
        comment.setId(0L);
        comment.setText(commentRequest.getText());
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(now);
        return comment;
    }

    public CommentResponse mapToCommentResponse(Comment comment) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setText(comment.getText());
        commentResponse.setAuthorName(comment.getAuthor().getName());
        commentResponse.setCreated(comment.getCreated());
        return commentResponse;
    }

    @Generated
    public List<CommentResponse> mapToCommentResponseList(List<Comment> comments) {
        List<CommentResponse> commentResponseList = new ArrayList<>();
        for (Comment comment : comments) {
            commentResponseList.add(mapToCommentResponse(comment));
        }
        return commentResponseList;
    }
}