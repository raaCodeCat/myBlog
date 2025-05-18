package ru.rakhmanov.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.rakhmanov.dto.response.PostFullDto;
import ru.rakhmanov.model.Comment;
import ru.rakhmanov.model.Tag;
import ru.rakhmanov.service.*;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PostController.class)
class PostControllerTest {

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private CommentService commentService;

    @MockitoBean
    private LikeService likeService;

    @MockitoBean
    private TagService tagService;

    @MockitoBean
    private FileStorageService fileStorageService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getPost_shouldReturnPostViewWithAttributes() throws Exception {
        int postId = 1;
        PostFullDto post = new PostFullDto();
        List<Comment> comments = Collections.singletonList(new Comment());
        List<Tag> tags = Collections.singletonList(new Tag());
        int likesCount = 5;

        when(postService.getPostById(postId)).thenReturn(post);
        when(commentService.getCommentsByPostId(postId)).thenReturn(comments);
        when(likeService.getLikesCountByPostId(postId)).thenReturn(likesCount);
        when(tagService.getAllTags()).thenReturn(tags);

        mockMvc.perform(get("/posts/{id}", postId))
                .andExpect(status().isOk())
                .andExpect(view().name("blog/post"))
                .andExpect(model().attribute("post", post))
                .andExpect(model().attribute("comments", comments))
                .andExpect(model().attribute("likesCount", likesCount))
                .andExpect(model().attribute("tags", tags));

        verify(postService, times(1)).getPostById(postId);
        verify(commentService, times(1)).getCommentsByPostId(postId);
        verify(likeService, times(1)).getLikesCountByPostId(postId);
        verify(tagService, times(1)).getAllTags();
    }

    @Test
    void createPost_shouldSavePostAndRedirect() throws Exception {
        String title = "Test Post";
        String content = "Test Content";
        List<Integer> tags = List.of(1, 2);
        MockMultipartFile image = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "test image".getBytes());

        when(fileStorageService.saveImage(any())).thenReturn("/uploads/mocked.jpg");
        doNothing().when(postService).createPost(eq(title), anyString(), eq(content), eq(tags));

        mockMvc.perform(multipart("/posts/add")
                        .file(image)
                        .param("title", title)
                        .param("content", content)
                        .param("tags", "1", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(postService, times(1)).createPost(eq(title), anyString(), eq(content), eq(tags));
    }

    @Test
    void editPost_shouldUpdatePostAndRedirect() throws Exception {
        int postId = 1;
        String title = "Updated Post";
        String content = "Updated Content";
        List<Integer> tags = List.of(3, 4);
        MockMultipartFile image = new MockMultipartFile(
                "image", "update.jpg", "image/jpeg", "updated image".getBytes());

        when(fileStorageService.saveImage(any())).thenReturn("/uploads/mocked.jpg");
        doNothing().when(postService).editPost(eq(postId), eq(title), anyString(), eq(content), eq(tags));

        mockMvc.perform(multipart("/posts/{id}/edit", postId)
                        .file(image)
                        .param("title", title)
                        .param("content", content)
                        .param("tags", "3", "4"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + postId));

        verify(postService, times(1)).editPost(eq(postId), eq(title), anyString(), eq(content), eq(tags));
    }

    @Test
    void createPost_shouldHandleFileStorageException() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "test image".getBytes());

        when(fileStorageService.saveImage(any())).thenThrow(new RuntimeException("File storage error"));

        mockMvc.perform(multipart("/posts/add")
                        .file(image)
                        .param("title", "Test Title")
                        .param("content", "Test Content"))
                .andExpect(status().isOk())
                .andExpect(view().name("blog/error"));
    }

    @Test
    void deletePost_shouldRemovePostAndRedirect() throws Exception {
        int postId = 1;

        doNothing().when(postService).deletePostById(postId);

        mockMvc.perform(post("/posts/{id}/delete", postId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(postService, times(1)).deletePostById(postId);
    }

    @Test
    void addComment_shouldSaveCommentAndRedirect() throws Exception {
        int postId = 1;
        String commentText = "New comment";

        doNothing().when(commentService).addComment(postId, commentText);

        mockMvc.perform(post("/posts/{id}/comments/add", postId)
                        .param("commentText", commentText))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + postId));

        verify(commentService, times(1)).addComment(postId, commentText);
    }

    @Test
    void likePost_shouldIncrementLikeCountAndRedirect() throws Exception {
        int postId = 1;

        doNothing().when(postService).likePost(postId);

        mockMvc.perform(post("/posts/{id}/like", postId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + postId));

        verify(postService, times(1)).likePost(postId);
    }
}