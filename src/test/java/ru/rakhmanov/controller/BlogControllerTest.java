package ru.rakhmanov.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.rakhmanov.dto.response.PostFullDto;
import ru.rakhmanov.model.Tag;
import ru.rakhmanov.service.CommentService;
import ru.rakhmanov.service.PostService;
import ru.rakhmanov.service.TagService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BlogControllerTest {

    @Mock
    private PostService postService;

    @Mock
    private TagService tagService;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private BlogController blogController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(blogController)
                .addPlaceholderValue("server.servlet.context-path", "")
                .build();
    }

    @Test
    void getBlogPage_shouldReturnCorrectView() throws Exception {
        List<PostFullDto> posts = Collections.singletonList(new PostFullDto());
        List<Tag> tags = Collections.singletonList(new Tag());

        when(postService.getAllPosts(any(), anyInt(), anyInt())).thenReturn(posts);
        when(tagService.getAllTags()).thenReturn(tags);
        when(postService.getPostCount(any())).thenReturn(1);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("blog/index"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("tags"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"));

        verify(postService, times(1)).getAllPosts(null, 0, 10);
        verify(tagService, times(1)).getAllTags();
        verify(postService, times(1)).getPostCount(null);
    }

    @Test
    void getBlogPage_withTagFilter_shouldPassTagIdToService() throws Exception {
        int tagId = 1;
        when(postService.getAllPosts(eq(tagId), anyInt(), anyInt())).thenReturn(Collections.emptyList());
        when(tagService.getAllTags()).thenReturn(Collections.emptyList());
        when(postService.getPostCount(eq(tagId))).thenReturn(0);

        mockMvc.perform(get("/").param("tagId", String.valueOf(tagId)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("selectedTagId", tagId));

        verify(postService, times(1)).getAllPosts(tagId, 0, 10);
        verify(postService, times(1)).getPostCount(tagId);
    }

    @Test
    void getBlogPage_withPagination_shouldPassPageAndSizeToService() throws Exception {
        int page = 2;
        int size = 5;
        when(postService.getAllPosts(any(), eq(page), eq(size))).thenReturn(Collections.emptyList());
        when(tagService.getAllTags()).thenReturn(Collections.emptyList());
        when(postService.getPostCount(any())).thenReturn(10);

        mockMvc.perform(get("/")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("currentPage", page))
                .andExpect(model().attribute("selectedSize", size));

        verify(postService, times(1)).getAllPosts(null, page, size);
        verify(tagService, times(1)).getAllTags();
        verify(postService, times(1)).getPostCount(any());
    }

    @Test
    void getBlogPage_withNegativeTagId_shouldTreatAsNull() throws Exception {
        when(postService.getAllPosts(isNull(), anyInt(), anyInt())).thenReturn(Collections.emptyList());
        when(tagService.getAllTags()).thenReturn(Collections.emptyList());
        when(postService.getPostCount(isNull())).thenReturn(0);

        mockMvc.perform(get("/").param("tagId", "-1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("selectedTagId", -1));

        verify(postService, times(1)).getAllPosts(null, 0, 10);
        verify(tagService, times(1)).getAllTags();
        verify(postService, times(1)).getPostCount(null);
    }

    @Test
    void getBlogPage_shouldCalculateTotalPagesCorrectly() throws Exception {
        int postsCount = 25;
        int size = 5;
        int expectedTotalPages = 5;

        when(postService.getAllPosts(any(), anyInt(), anyInt())).thenReturn(Collections.emptyList());
        when(tagService.getAllTags()).thenReturn(Collections.emptyList());
        when(postService.getPostCount(any())).thenReturn(postsCount);

        mockMvc.perform(get("/").param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalPages", expectedTotalPages));
    }
}