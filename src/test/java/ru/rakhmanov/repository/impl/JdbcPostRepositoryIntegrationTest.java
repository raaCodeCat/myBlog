package ru.rakhmanov.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.rakhmanov.config.TestConfig;
import ru.rakhmanov.model.Post;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class JdbcPostRepositoryIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcPostRepository postRepository;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.execute("""
            set referential_integrity false;
            truncate table posttags restart identity;
            truncate table tags restart identity;
            truncate table posts restart identity;
            set referential_integrity true;
        """);

        jdbcTemplate.update("insert into posts (post_title, post_content, post_image_url) VALUES (?, ?, ?)",
                "Post 1", "Content 1", "image1.jpg");
        jdbcTemplate.update("insert into posts (post_title, post_content, post_image_url) VALUES (?, ?, ?)",
                "Post 2", "Content 2", "image2.jpg");

        jdbcTemplate.update("insert into tags (tag_name) values (?)", "Tag1");
        jdbcTemplate.update("insert into tags (tag_name) values (?)", "Tag2");

        jdbcTemplate.update("insert into posttags (post_id, tag_id) values (?, ?)", 1, 1);
        jdbcTemplate.update("insert into posttags (post_id, tag_id) values (?, ?)", 1, 2);
        jdbcTemplate.update("insert into posttags (post_id, tag_id) values (?, ?)", 2, 1);
    }

    @Test
    void findAllPosts_ShouldReturnListOfPosts() {
        List<Post> actual = postRepository.findAllPosts(null, 0, 10);

        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    void findPostById_ShouldReturnPost() {
        Post actual = postRepository.findPostById(1);

        assertNotNull(actual);
        assertEquals("Post 1", actual.getTitle());
    }

    @Test
    void deletePostById_ShouldDeletePost() {
        postRepository.deletePostById(1);

        Post actual = postRepository.findPostById(1);
        assertNull(actual);
    }

    @Test
    void countPosts_ShouldReturnCount() {
        Integer actual = postRepository.countPosts(null);

        assertNotNull(actual);
        assertEquals(2, actual);
    }

    @Test
    void savePost_ShouldSavePostAndReturnGeneratedId() {
        Post post = new Post(null, "New Title", "New Content", "new.jpg");

        Integer id = postRepository.savePost(post);
        Post actual = postRepository.findPostById(id);

        assertNotNull(actual);
        assertEquals(post.getTitle(), actual.getTitle());
        assertEquals(post.getContent(), actual.getContent());
        assertEquals(post.getImageUrl(), actual.getImageUrl());
    }

    @Test
    void updatePost_ShouldUpdatePost() {
        postRepository.updatePost(1, "Updated Title", "updated.jpg", "Updated Content");

        Post actual = postRepository.findPostById(1);
        assertEquals("Updated Title", actual.getTitle());
        assertEquals("Updated Content", actual.getContent());
        assertEquals("updated.jpg", actual.getImageUrl());
    }
}
