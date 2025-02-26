package ru.rakhmanov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rakhmanov.dto.response.PostResponseDto;
import ru.rakhmanov.service.PostService;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class BlogController {
    private final PostService postService;

    @GetMapping
    public String getBlogPage(Model model) {

//        List<PostResponseDto> posts = Arrays.asList(
//                new PostResponseDto(
//                        1L,
//                        "Первая запись в блоге",
//                        "Это текст первой записи в блоге. Здесь может быть много текста.",
//                        "somepath.png",
//                        Arrays.asList("новости", "технологии"),
//                        10L,
//                        2L
//                ),
//                new PostResponseDto(
//                        2L,
//                        "Вторая запись в блоге",
//                        "Это текст второй записи в блоге. Здесь тоже может быть много текста.",
//                        "somepath.png",
//                        Arrays.asList("путешествия", "фото"),
//                        11L,
//                        3L
//                )
//        );

        List<PostResponseDto> posts = postService.getAllPosts();

        model.addAttribute("posts", posts);
        return "blog";
    }
}

