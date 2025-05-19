package ru.rakhmanov.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.rakhmanov.config.MockJdbcTestConfig;
import ru.rakhmanov.model.PostTag;
import ru.rakhmanov.model.Tag;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MockJdbcTestConfig.class)
class JdbcTagRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcTagRepository tagRepository;

    private Tag tag1;
    private Tag tag2;
    private PostTag postTag1;
    private PostTag postTag2;

    @BeforeEach
    void setUp() {
        reset(jdbcTemplate);

        tag1 = new Tag(1, "Tag1");
        tag2 = new Tag(2, "Tag2");
        postTag1 = new PostTag(1, 1, "Tag1");
        postTag2 = new PostTag(1, 2, "Tag2");
    }

    @Test
    void findAllTags_ShouldReturnListOfTags() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(List.of(tag1, tag2));

        List<Tag> actual = tagRepository.findAllTags();

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals("Tag1", actual.get(0).getName());
        assertEquals("Tag2", actual.get(1).getName());

        verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class));
    }

    @Test
    void findTagsByPostId_ShouldReturnTagsForPost() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1)))
                .thenReturn(List.of(tag1, tag2));

        List<Tag> actual = tagRepository.findTagsByPostId(1);

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals("Tag1", actual.get(0).getName());
        assertEquals("Tag2", actual.get(1).getName());

        verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class), eq(1));
    }

    @Test
    void findTagsByPostIds_ShouldReturnTagsForPosts() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(List.of(postTag1, postTag2));

        Map<Integer, List<Tag>> actual = tagRepository.findTagsByPostIds(List.of(1));

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(2, actual.get(1).size());
        assertEquals("Tag1", actual.get(1).get(0).getName());
        assertEquals("Tag2", actual.get(1).get(1).getName());

        verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class));
    }

    @Test
    void saveTagsToPost_ShouldSaveTags() {
        when(jdbcTemplate.update(anyString())).thenReturn(1);

        tagRepository.saveTagsToPost(1, List.of(1, 2));

        verify(jdbcTemplate, times(1)).update(anyString());
    }

    @Test
    void deleteTagsFromPost_ShouldDeleteTags() {
        when(jdbcTemplate.update(anyString(), anyInt())).thenReturn(1);

        tagRepository.deleteTagsFromPost(1);

        verify(jdbcTemplate, times(1)).update(anyString(), anyInt());
    }

    @Test
    void findTagsByPostId_ShouldReturnEmptyListForNullPostId() {
        List<Tag> actual = tagRepository.findTagsByPostId(null);

        assertNotNull(actual);
        assertTrue(actual.isEmpty());

        verify(jdbcTemplate, never()).query(anyString(), any(RowMapper.class), any());
    }

    @Test
    void findTagsByPostIds_ShouldReturnEmptyMapForEmptyPostIds() {
        Map<Integer, List<Tag>> actual = tagRepository.findTagsByPostIds(List.of());

        assertNotNull(actual);
        assertTrue(actual.isEmpty());

        verify(jdbcTemplate, never()).query(anyString(), any(RowMapper.class));
    }
}