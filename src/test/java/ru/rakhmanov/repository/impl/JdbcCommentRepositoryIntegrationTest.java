package ru.rakhmanov.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.rakhmanov.config.RepositoryIntegrationTestConfig;
import ru.rakhmanov.model.Comment;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = RepositoryIntegrationTestConfig.class)
class JdbcCommentRepositoryIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcCommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("""
            set referential_integrity false;
            truncate table comments restart identity;
            truncate table posts restart identity;
            set referential_integrity true;
        """);

        jdbcTemplate.update(
                "insert into posts (post_title, post_content, post_image_url) values (?, ?, ?)",
                "Post 1", "Content 1", "image1.jpg"
        );
        jdbcTemplate.update(
                "insert into posts (post_title, post_content, post_image_url) values (?, ?, ?)",
                "Post 2", "Content 2", "image2.jpg"
        );

        jdbcTemplate.update(
                "insert into comments (post_id, comment_content) values (?, ?)",
                1, "Comment 1 for post 1"
        );
        jdbcTemplate.update(
                "insert into comments (post_id, comment_content) values (?, ?)",
                1, "Comment 2 for post 1"
        );
        jdbcTemplate.update(
                "insert into comments (post_id, comment_content) values (?, ?)",
                2, "Comment 1 for post 2"
        );
    }

    @Test
    void saveComment_ShouldSaveCommentToDatabase() {
        commentRepository.saveComment(1, "New comment");

        Integer count = jdbcTemplate.queryForObject(
                "select count(*) from comments where post_id = 1 and comment_content = 'New comment'",
                Integer.class
        );

        assertEquals(1, count);
    }

    @Test
    void getCommentsByPostId_ShouldReturnCommentsForPost() {
        List<Comment> comments = commentRepository.getCommentsByPostId(1);

        assertNotNull(comments);
        assertEquals(2, comments.size());
        assertEquals("Comment 1 for post 1", comments.get(0).getContent());
        assertEquals("Comment 2 for post 1", comments.get(1).getContent());
    }

    @Test
    void getCommentsByPostId_ShouldReturnEmptyListForNonExistingPost() {
        List<Comment> comments = commentRepository.getCommentsByPostId(999);

        assertNotNull(comments);
        assertTrue(comments.isEmpty());
    }

    @Test
    void getCommentsCountByPostIds_ShouldReturnCorrectCounts() {
        Map<Integer, Integer> counts = commentRepository.getCommentsCountByPostIds(List.of(1, 2));

        assertNotNull(counts);
        assertEquals(2, counts.size());
        assertEquals(2, counts.get(1));
        assertEquals(1, counts.get(2));
    }

    @Test
    void getCommentsCountByPostIds_ShouldReturnEmptyMapForEmptyInput() {
        Map<Integer, Integer> counts = commentRepository.getCommentsCountByPostIds(List.of());

        assertNotNull(counts);
        assertTrue(counts.isEmpty());
    }

    @Test
    void editComment_ShouldUpdateCommentContent() {
        commentRepository.editComment(1, "Updated comment");

        String content = jdbcTemplate.queryForObject(
                "select comment_content from comments where comment_id = 1",
                String.class
        );

        assertEquals("Updated comment", content);
    }

    @Test
    void deleteComment_ShouldRemoveCommentFromDatabase() {
        commentRepository.deleteComment(1);

        Integer count = jdbcTemplate.queryForObject(
                "select count(*) from comments where comment_id = 1",
                Integer.class
        );

        assertEquals(0, count);
    }

    @Test
    void getCommentsByPostId_ShouldReturnEmptyListForNullPostId() {
        List<Comment> comments = commentRepository.getCommentsByPostId(null);

        assertNotNull(comments);
        assertTrue(comments.isEmpty());
    }
}