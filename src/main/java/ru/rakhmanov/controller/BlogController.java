package ru.rakhmanov.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.rakhmanov.dto.response.PostResponseDto;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/")
public class BlogController {
    @GetMapping
    public String getBlogPage(Model model) {

        List<PostResponseDto> posts = Arrays.asList(
                new PostResponseDto(
                        1L,
                        "Первая запись в блоге",
                        "Это текст первой записи в блоге. Здесь может быть много текста.",
                        "somepath.png",
                        Arrays.asList("новости", "технологии"),
                        10L,
                        2L
                ),
                new PostResponseDto(
                        2L,
                        "Вторая запись в блоге",
                        "Это текст второй записи в блоге. Здесь тоже может быть много текста.",
                        "somepath.png",
                        Arrays.asList("путешествия", "фото"),
                        11L,
                        3L
                )
        );

        model.addAttribute("posts", posts);
        return "blog";
    }
}

