package ru.rakhmanov.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.rakhmanov.service.CommentService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {

    @MockitoBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void editComment_shouldUpdateCommentAndRedirect() throws Exception {
        int commentId = 1;
        int postId = 10;
        String newText = "Updated comment text";

        doNothing().when(commentService).editComment(eq(commentId), eq(newText));

        mockMvc.perform(post("/comments/{id}/edit", commentId)
                        .param("commentText", newText)
                        .param("postId", String.valueOf(postId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + postId));

        verify(commentService, times(1)).editComment(eq(commentId), eq(newText));
    }

    @Test
    void deleteComment_shouldRemoveCommentAndRedirect() throws Exception {
         int commentId = 1;
        int postId = 10;

        doNothing().when(commentService).deleteComment(eq(commentId));

        mockMvc.perform(post("/comments/{id}/delete", commentId)
                        .param("postId", String.valueOf(postId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + postId));

        verify(commentService, times(1)).deleteComment(eq(commentId));
    }
}