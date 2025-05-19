package ru.rakhmanov.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.rakhmanov.config.RepositoryIntegrationTestConfig;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = RepositoryIntegrationTestConfig.class)
class JdbcLikeRepositoryIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcLikeRepository likeRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("""
            set referential_integrity false;
            truncate table postlikes restart identity;
            truncate table posts restart identity;
            set referential_integrity true;
        """);

        jdbcTemplate.update("insert into posts (post_title, post_content, post_image_url) values (?, ?, ?)",
                "Post 1", "Content 1", "image1.jpg");

        jdbcTemplate.update("insert into posts (post_title, post_content, post_image_url) values (?, ?, ?)",
                "Post 2", "Content 2", "image2.jpg");

        jdbcTemplate.update("insert into postlikes (post_id) values (?)", 1);
        jdbcTemplate.update("insert into postlikes (post_id) values (?)", 1);
        jdbcTemplate.update("insert into postlikes (post_id) values (?)", 1);
        jdbcTemplate.update("insert into postlikes (post_id) values (?)", 2);
    }

    @Test
    void addLikeToPost_ShouldIncrementLikeCount() {
        Integer initialCount = likeRepository.getLikesCountByPostId(1);

        likeRepository.addLikeToPost(1);

        assertEquals(initialCount + 1, likeRepository.getLikesCountByPostId(1));
    }

    @Test
    void getLikesCountByPostId_ShouldReturnCorrectCount() {
        Integer likesCountByPostId1 = likeRepository.getLikesCountByPostId(1);
        Integer likesCountByPostId2 = likeRepository.getLikesCountByPostId(2);
        Integer likesCountByPostIdNotCorrect = likeRepository.getLikesCountByPostId(3);

        assertEquals(3, likesCountByPostId1);
        assertEquals(1, likesCountByPostId2);
        assertEquals(0, likesCountByPostIdNotCorrect);
    }

    @Test
    void getLikesCountByPostId_ShouldReturnZeroForNullPostId() {
        Integer likesCount = likeRepository.getLikesCountByPostId(null);

        assertEquals(0, likesCount);
    }

    @Test
    void getLikesCountByPostIds_ShouldReturnCorrectMap() {
        Map<Integer, Integer> counts = likeRepository.getLikesCountByPostIds(List.of(1, 2, 3));

        assertNotNull(counts);
        assertEquals(2, counts.size());
        assertEquals(3, counts.get(1));
        assertEquals(1, counts.get(2));
        assertNull(counts.get(3));
    }

    @Test
    void getLikesCountByPostIds_ShouldReturnEmptyMapForEmptyList() {
        Map<Integer, Integer> counts = likeRepository.getLikesCountByPostIds(List.of());

        assertNotNull(counts);
        assertTrue(counts.isEmpty());
    }

    @Test
    void getLikesCountByPostIds_ShouldReturnEmptyMapForNonExistingPosts() {
        Map<Integer, Integer> counts = likeRepository.getLikesCountByPostIds(List.of(3, 4));

        assertNotNull(counts);
        assertTrue(counts.isEmpty());
    }

    @Test
    void postLikeCountRowMapper_ShouldMapCorrectly() {
        jdbcTemplate.update("insert into postlikes (post_id) values (?)", 1);

        List<JdbcLikeRepository.PostLikeCount> result = jdbcTemplate.query(
                "select post_id, count(*) as like_count from postlikes where post_id = 1 group by post_id",
                JdbcLikeRepository.postLikeCountRowMapper()
        );

        Integer newPostLikeCount = result.get(0).getLikeCount();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getPostId());
        assertEquals(4, newPostLikeCount);
    }
}