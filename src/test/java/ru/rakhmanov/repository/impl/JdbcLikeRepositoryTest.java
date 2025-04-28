package ru.rakhmanov.repository.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JdbcLikeRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private JdbcLikeRepository likeRepository;

    @Test
    void addLikeToPost_ShouldExecuteInsert() {
        Integer postId = 1;

        likeRepository.addLikeToPost(postId);

        verify(jdbcTemplate, times(1)).update(anyString(), eq(postId));
    }

    @Test
    void getLikesCountByPostId_ShouldReturnCount() {
        Integer postId = 1;
        Integer expected = 5;

        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(postId)))
                .thenReturn(expected);

        Integer actual = likeRepository.getLikesCountByPostId(postId);

        assertEquals(expected, actual);
        verify(jdbcTemplate).queryForObject(anyString(), eq(Integer.class), eq(postId));
    }

    @Test
    void getLikesCountByPostId_ShouldReturnZeroWhenNull() {
        Integer postId = 1;

        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(postId)))
                .thenReturn(10);

        Integer actual = likeRepository.getLikesCountByPostId(postId);

        assertEquals(10, actual);
    }

    @Test
    void getLikesCountByPostIds_ShouldReturnEmptyMapForEmptyInput() {
        Map<Integer, Integer> result = likeRepository.getLikesCountByPostIds(Collections.emptyList());

        assertTrue(result.isEmpty());
        verifyNoInteractions(jdbcTemplate);
    }

    @Test
    void getLikesCountByPostIds_ShouldReturnCorrectMap() {
        List<Integer> postIds = List.of(1, 2);

        List<JdbcLikeRepository.PostLikeCount> mockResults = List.of(
                new JdbcLikeRepository.PostLikeCount(1, 10),
                new JdbcLikeRepository.PostLikeCount(2, 5)
        );

        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(mockResults);

        Map<Integer, Integer> result = likeRepository.getLikesCountByPostIds(postIds);

        assertEquals(2, result.size());
        assertEquals(10, result.get(1));
        assertEquals(5, result.get(2));
        verify(jdbcTemplate).query(anyString(), any(RowMapper.class));
    }

    @Test
    void postLikeCountRowMapper_ShouldMapCorrectly() throws Exception {
        RowMapper<JdbcLikeRepository.PostLikeCount> mapper = JdbcLikeRepository.postLikeCountRowMapper();
        java.sql.ResultSet rs = mock(java.sql.ResultSet.class);

        when(rs.getInt("post_id")).thenReturn(1);
        when(rs.getInt("like_count")).thenReturn(5);

        JdbcLikeRepository.PostLikeCount result = mapper.mapRow(rs, 1);

        assertEquals(1, result.getPostId());
        assertEquals(5, result.getLikeCount());
    }
}