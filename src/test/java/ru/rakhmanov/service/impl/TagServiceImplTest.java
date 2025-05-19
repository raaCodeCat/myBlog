package ru.rakhmanov.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.rakhmanov.model.Tag;
import ru.rakhmanov.repository.TagRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = TagServiceImpl.class)
class TagServiceImplTest {

    @MockitoBean
    private TagRepository tagRepository;

    @Autowired
    private TagServiceImpl tagService;

    private List<Tag> tags;

    @BeforeEach
    void setUp() {
        tags = List.of(
                new Tag(1, "Tag1"),
                new Tag(2, "Tag2"),
                new Tag(3, "Tag3")
        );
    }

    @Test
    void getAllTags_ShouldReturnListOfTags() {
        when(tagRepository.findAllTags()).thenReturn(tags);

        List<Tag> actual = tagService.getAllTags();

        assertNotNull(actual);
        assertEquals(3, actual.size());
        assertEquals("Tag1", actual.get(0).getName());
        assertEquals("Tag2", actual.get(1).getName());
        assertEquals("Tag3", actual.get(2).getName());

        verify(tagRepository, times(1)).findAllTags();
    }

    @Test
    void getAllTags_ShouldReturnEmptyListWhenNoTags() {
        when(tagRepository.findAllTags()).thenReturn(List.of());

        List<Tag> actual = tagService.getAllTags();

        assertNotNull(actual);
        assertTrue(actual.isEmpty());

        verify(tagRepository, times(1)).findAllTags();
    }
}