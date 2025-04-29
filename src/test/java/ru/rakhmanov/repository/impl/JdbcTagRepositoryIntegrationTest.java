package ru.rakhmanov.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.rakhmanov.model.Tag;
import ru.rakhmanov.config.TestConfig;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class JdbcTagRepositoryIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcTagRepository tagRepository;

    @BeforeEach
    void setUp() {
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
    void findAllTags_ShouldReturnAllTags() {
        List<Tag> tags = tagRepository.findAllTags();
        
        assertNotNull(tags);
        assertEquals(2, tags.size());
        assertEquals("Tag1", tags.get(0).getName());
        assertEquals("Tag2", tags.get(1).getName());
    }

    @Test
    void findTagsByPostId_ShouldReturnTagsForPost() {
        List<Tag> tags = tagRepository.findTagsByPostId(1);
        
        assertNotNull(tags);
        assertEquals(2, tags.size());
        assertEquals("Tag1", tags.get(0).getName());
        assertEquals("Tag2", tags.get(1).getName());
    }

    @Test
    void findTagsByPostIds_ShouldReturnTagsForPosts() {
        Map<Integer, List<Tag>> tagsByPostIds = tagRepository.findTagsByPostIds(List.of(1, 2));
        
        assertNotNull(tagsByPostIds);
        assertEquals(2, tagsByPostIds.size());
        assertEquals(2, tagsByPostIds.get(1).size());
        assertEquals(1, tagsByPostIds.get(2).size());
    }

    @Test
    void saveTagsToPost_ShouldSaveTags() {
        tagRepository.saveTagsToPost(2, List.of(2));
        
        List<Tag> tags = tagRepository.findTagsByPostId(2);
        assertEquals(2, tags.size());
        assertEquals("Tag2", tags.get(1).getName());
    }

    @Test
    void deleteTagsFromPost_ShouldDeleteTags() {
        tagRepository.deleteTagsFromPost(1);
        
        List<Tag> tags = tagRepository.findTagsByPostId(1);
        assertTrue(tags.isEmpty());
    }

    @Test
    void findTagsByPostId_ShouldReturnEmptyListForNullPostId() {
        List<Tag> tags = tagRepository.findTagsByPostId(null);
        
        assertNotNull(tags);
        assertTrue(tags.isEmpty());
    }

    @Test
    void findTagsByPostIds_ShouldReturnEmptyMapForEmptyPostIds() {
        Map<Integer, List<Tag>> tagsByPostIds = tagRepository.findTagsByPostIds(List.of());
        
        assertNotNull(tagsByPostIds);
        assertTrue(tagsByPostIds.isEmpty());
    }
}