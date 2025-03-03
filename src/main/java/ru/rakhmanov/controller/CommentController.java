package ru.rakhmanov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.rakhmanov.service.CommentService;

@Controller
@RequestMapping("comments/{id}")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/edit")
    public String editComment(@PathVariable(name = "id") Integer id,
                              @RequestParam("commentText") String commentText,
                              @RequestParam("postId") Integer postId) {

        commentService.editComment(id, commentText);

        return "redirect:/posts/" + postId;
    }

    @PostMapping("/delete")
    public String deleteComment(@PathVariable(name = "id") Integer id,
                                @RequestParam("postId") Integer postId) {
        commentService.deleteComment(id);

        return "redirect:/posts/" + postId;
    }
}
