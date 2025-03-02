package ru.rakhmanov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rakhmanov.dto.response.PostFullDto;
import ru.rakhmanov.service.PostService;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class BlogController {
    private final PostService postService;

    @GetMapping
    public String getBlogPage(Model model) {

        List<PostFullDto> posts = postService.getAllPosts();

        model.addAttribute("posts", posts);
        return "blog/mainpage";
    }
}

