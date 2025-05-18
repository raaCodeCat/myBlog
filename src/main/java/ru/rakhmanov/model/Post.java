package ru.rakhmanov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private Integer id;
    private String title;
    private String content;
    private String imageUrl;
    private Integer likesCount;
    private Integer commentsCount;

    public Post (Integer id, String title, String content, String imageUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.likesCount = 0;
        this.commentsCount = 0;
    }
}
