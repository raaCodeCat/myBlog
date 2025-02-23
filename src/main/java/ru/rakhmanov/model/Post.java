package ru.rakhmanov.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Post {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private List<String> tags;
}
