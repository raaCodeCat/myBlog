package ru.rakhmanov.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rakhmanov.model.Comment;
import ru.rakhmanov.repository.CommentRepository;
import ru.rakhmanov.service.CommentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public void addComment(Integer postId, String commentText) {
        commentRepository.saveComment(postId, commentText);
    }

    @Override
    public List<Comment> getCommentsByPostId(Integer postId) {
        return commentRepository.getCommentsByPostId(postId);
    }

    @Override
    public Integer getCommentCountByPostId(Integer postId) {
        return getCommentCountByPostId(postId);
    }
}
