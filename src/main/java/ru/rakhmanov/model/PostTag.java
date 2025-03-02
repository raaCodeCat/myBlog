package ru.rakhmanov.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostTag {
    private Integer postId;
    private Integer tagId;
    private String tagName;
}
