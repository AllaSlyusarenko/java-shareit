package ru.practicum.shareit.item.comment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {
    public static Comment mapToComment(CommentRequest commentRequest, User user, Item item, LocalDateTime now) {
        Comment comment = new Comment();
        comment.setText(commentRequest.getText());
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(now);
        return comment;
    }

    public static CommentResponse mapToCommentResponse(Comment comment) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setText(comment.getText());
        commentResponse.setItem(comment.getItem());
        commentResponse.setAuthorName(comment.getAuthor().getName());
        commentResponse.setCreated(comment.getCreated());
        return commentResponse;
    }
}