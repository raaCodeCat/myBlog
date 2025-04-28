package ru.rakhmanov.repository.impl;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.rakhmanov.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JdbcPostRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @InjectMocks
    private JdbcPostRepository postRepository;

    private Post post;

    @BeforeEach
    void setUp() {
        post = new Post(1, "Test Title", "Test Content", "test.jpg");
    }

    @Test
    void findAllPosts_ShouldReturnListOfPosts() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(List.of(post));

        List<Post> actual = postRepository.findAllPosts(null, 0, 10);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals("Test Title", actual.get(0).getTitle());

        verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class));
    }

    @Test
    void findPostById_ShouldReturnPost() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyInt()))
                .thenReturn(List.of(post));

        Post actual = postRepository.findPostById(1);

        assertNotNull(actual);
        assertEquals(post.getId(), actual.getId());
        assertEquals(post.getTitle(), actual.getTitle());
        assertEquals(post.getContent(), actual.getContent());
        assertEquals(post.getImageUrl(), actual.getImageUrl());

        verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class), anyInt());
    }

    @Test
    void deletePostById_ShouldDeletePost() {
        when(jdbcTemplate.update(anyString(), eq(1))).thenReturn(1);

        postRepository.deletePostById(1);

        verify(jdbcTemplate, times(1)).update(anyString(), eq(1));
    }

    @Test
    @SneakyThrows
    void savePost_ShouldSavePostAndReturnId() {
        Post newPost = new Post(null, "Test Title", "Test Content", "test.jpg");
        int expectedId = 1;

        when(jdbcTemplate.update(any(PreparedStatementCreator.class), any()))
                .thenAnswer(invocation -> {
                    Object[] args = invocation.getArguments();
                    Map<String, Object> keyMap = Collections.singletonMap("post_id", expectedId);
                    ((GeneratedKeyHolder) args[1]).getKeyList().add(keyMap);
                    return 1;
                });

        Integer actual = postRepository.savePost(newPost);

        assertNotNull(actual);
        assertEquals(expectedId, actual);

        verify(jdbcTemplate, times(1))
                .update(any(PreparedStatementCreator.class), any(KeyHolder.class));
    }

    @Test
    void updatePost_ShouldUpdatePost() {
        when(jdbcTemplate.update(anyString(), eq("New Title"), eq("New Content"), eq("new.jpg"), eq(1)))
                .thenReturn(1);

        postRepository.updatePost(1, "New Title", "new.jpg", "New Content");

        verify(jdbcTemplate, times(1)).update(anyString(), eq("New Title"), eq("New Content"), eq("new.jpg"), eq(1));
    }

    @Test
    void findAllPosts_ShouldFilterByTagId() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(List.of(post));

        List<Post> actual = postRepository.findAllPosts(1, 0, 10);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(post.getTitle(), actual.get(0).getTitle());
        assertEquals(post.getId(), actual.get(0).getId());
        assertEquals(post.getTitle(), actual.get(0).getTitle());
        assertEquals(post.getContent(), actual.get(0).getContent());
        assertEquals(post.getImageUrl(), actual.get(0).getImageUrl());

        verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class));
    }

    @Test
    void findAllPosts_ShouldApplyPagination() {

        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(List.of(post));

        List<Post> actual = postRepository.findAllPosts(null, 1, 10);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(post.getId(), actual.get(0).getId());
        assertEquals(post.getTitle(), actual.get(0).getTitle());
        assertEquals(post.getContent(), actual.get(0).getContent());
        assertEquals(post.getImageUrl(), actual.get(0).getImageUrl());

        verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class));
    }
}