package ru.rakhmanov.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Integer id;
    private String title;
    private String content;
    private String imageUrl;
    private List<String> tags;
    private Long likesCount = 0L;
    private Long commentsCount = 0L;
}
