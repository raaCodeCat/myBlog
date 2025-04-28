package ru.rakhmanov.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rakhmanov.model.Comment;
import ru.rakhmanov.repository.CommentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment comment;
    private List<Comment> comments;

    @BeforeEach
    void setUp() {
        comment = new Comment();
        comment.setId(1);
        comment.setPostId(1);
        comment.setContent("Test Comment");

        comments = List.of(comment);
    }

    @Test
    void addComment_ShouldSaveComment() {
        doNothing().when(commentRepository).saveComment(1, "Test Comment");

        commentService.addComment(1, "Test Comment");

        verify(commentRepository, times(1)).saveComment(1, "Test Comment");
    }

    @Test
    void getCommentsByPostId_ShouldReturnComments() {
        when(commentRepository.getCommentsByPostId(1)).thenReturn(comments);

        List<Comment> actual = commentService.getCommentsByPostId(1);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals("Test Comment", actual.get(0).getContent());

        verify(commentRepository, times(1)).getCommentsByPostId(1);
    }

    @Test
    void editComment_ShouldUpdateComment() {
        doNothing().when(commentRepository).editComment(1, "Updated Comment");

        commentService.editComment(1, "Updated Comment");

        verify(commentRepository, times(1)).editComment(1, "Updated Comment");
    }

    @Test
    void deleteComment_ShouldDeleteComment() {
        doNothing().when(commentRepository).deleteComment(1);

        commentService.deleteComment(1);

        verify(commentRepository, times(1)).deleteComment(1);
    }
}