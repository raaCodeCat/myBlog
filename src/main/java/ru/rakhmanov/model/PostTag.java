package ru.rakhmanov.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostTag {
    private Integer postId;
    private Integer tagId;
    private String tagName;
}
