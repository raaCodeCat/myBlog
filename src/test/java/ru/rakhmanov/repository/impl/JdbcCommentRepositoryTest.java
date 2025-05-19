package ru.rakhmanov.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.rakhmanov.config.MockJdbcTestConfig;
import ru.rakhmanov.model.Comment;

import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MockJdbcTestConfig.class)
class JdbcCommentRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcCommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        reset(jdbcTemplate);
    }

    @Test
    void saveComment_ShouldExecuteInsert() {
        Integer postId = 1;
        String commentText = "Test comment";

        commentRepository.saveComment(postId, commentText);

        verify(jdbcTemplate, times(1)).update(anyString(), eq(postId), eq(commentText));
    }

    @Test
    void getCommentsByPostId_ShouldReturnComments() {
        Integer postId = 1;
        List<Comment> expectedComments = List.of(
                new Comment(1, postId, "Comment 1"),
                new Comment(2, postId, "Comment 2")
        );

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(postId)))
                .thenReturn(expectedComments);

        List<Comment> actualComments = commentRepository.getCommentsByPostId(postId);

        assertEquals(expectedComments, actualComments);
        verify(jdbcTemplate, times(1))
                .query(anyString(), any(RowMapper.class), eq(postId));
    }

    @Test
    void getCommentsCountByPostIds_ShouldReturnEmptyMapForEmptyInput() {
        Map<Integer, Integer> result = commentRepository.getCommentsCountByPostIds(Collections.emptyList());

        assertTrue(result.isEmpty());
        verifyNoInteractions(jdbcTemplate);
    }

    @Test
    void getCommentsCountByPostIds_ShouldReturnCorrectMap() {
        List<Integer> postIds = List.of(1, 2);
        List<JdbcCommentRepository.PostCommentCount> mockResults = List.of(
                new JdbcCommentRepository.PostCommentCount(1, 3),
                new JdbcCommentRepository.PostCommentCount(2, 5)
        );

        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(mockResults);

        Map<Integer, Integer> result = commentRepository.getCommentsCountByPostIds(postIds);

        assertEquals(2, result.size());
        assertEquals(3, result.get(1));
        assertEquals(5, result.get(2));
        verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class));
    }

    @Test
    void editComment_ShouldExecuteUpdate() {
        Integer commentId = 1;
        String newText = "Updated comment";

        commentRepository.editComment(commentId, newText);

        verify(jdbcTemplate, times(1)).update(anyString(), eq(newText), eq(commentId));
    }

    @Test
    void deleteComment_ShouldExecuteDelete() {
        Integer commentId = 1;

        commentRepository.deleteComment(commentId);

        verify(jdbcTemplate, times(1)).update(anyString(), eq(commentId));
    }

    @Test
    void commentRowMapper_ShouldMapCorrectly() throws Exception {
        RowMapper<Comment> mapper = JdbcCommentRepository.commentRowMapper();
        ResultSet rs = mock(ResultSet.class);

        when(rs.getInt("comment_id")).thenReturn(1);
        when(rs.getInt("post_id")).thenReturn(10);
        when(rs.getString("comment_content")).thenReturn("Test comment");

        Comment result = mapper.mapRow(rs, 1);

        assertEquals(1, result.getId());
        assertEquals(10, result.getPostId());
        assertEquals("Test comment", result.getContent());
    }

    @Test
    void postCommentCountRowMapper_ShouldMapCorrectly() throws Exception {
        RowMapper<JdbcCommentRepository.PostCommentCount> mapper = JdbcCommentRepository.postCommentCountRowMapper();
        ResultSet rs = mock(ResultSet.class);

        when(rs.getInt("post_id")).thenReturn(1);
        when(rs.getInt("comment_count")).thenReturn(5);

        JdbcCommentRepository.PostCommentCount result = mapper.mapRow(rs, 1);

        assertEquals(1, result.getPostId());
        assertEquals(5, result.getCommentCount());
    }
}