package ru.rakhmanov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.rakhmanov.dto.response.PostFullDto;
import ru.rakhmanov.model.Tag;
import ru.rakhmanov.service.PostService;
import ru.rakhmanov.service.TagService;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class BlogController {
    private final PostService postService;

    private final TagService tagService;

    @GetMapping
    public String getBlogPage(@RequestParam(name = "tagId", required = false) Integer selectedTagId,
                              @RequestParam(name = "page", defaultValue = "0") Integer page,
                              @RequestParam(name = "size", defaultValue = "10") Integer size,
                              Model model) {

        Integer tagId = selectedTagId == null || selectedTagId < 0 ? null : selectedTagId;

        List<PostFullDto> posts = postService.getAllPosts(tagId, page, size);
        List<Tag> tags = tagService.getAllTags();
        Integer postsCount = postService.getPostCount(tagId);
        int totalPages = (int) Math.ceil((double) postsCount / size);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("posts", posts);
        model.addAttribute("tags", tags);
        model.addAttribute("selectedTagId", selectedTagId);
        model.addAttribute("selectedSize", size);
        model.addAttribute("selectedPage", page);
        return "blog/index";
    }
}

