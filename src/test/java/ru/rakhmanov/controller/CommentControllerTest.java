package ru.rakhmanov.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.rakhmanov.service.CommentService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .addPlaceholderValue("server.servlet.context-path", "")
                .build();
    }

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