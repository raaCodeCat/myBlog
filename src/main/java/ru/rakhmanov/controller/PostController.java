package ru.rakhmanov.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rakhmanov.dto.response.PostFullDto;
import ru.rakhmanov.service.PostService;

@Controller
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    public String getPost(@PathVariable("id") Integer id, Model model) {
        PostFullDto post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "blog/post";
    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable("id") Integer id, Model model) {
        postService.deletePostById(id);

        return "redirect:/";
    }

}
