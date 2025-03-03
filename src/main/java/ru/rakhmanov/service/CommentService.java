package ru.rakhmanov.service;

import ru.rakhmanov.model.Comment;

import java.util.List;

public interface CommentService {

    void addComment(Integer postId, String commentText);

    List<Comment> getCommentsByPostId(Integer postId);

    Integer getCommentCountByPostId(Integer postId);

    void deleteComment(Integer commentId);

    void editComment(Integer commentId, String commentText);

}
