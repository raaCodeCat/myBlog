package ru.rakhmanov.repository;

import ru.rakhmanov.model.Comment;

import java.util.List;
import java.util.Map;

public interface CommentRepository {

    void saveComment(Integer postId, String commentText);

    List<Comment> getCommentsByPostId(Integer postId);

    Integer countCommentsByPostId(Integer postId);

    Map<Integer, Integer> getCommentsCountByPostIds(List<Integer> postIds);

}
