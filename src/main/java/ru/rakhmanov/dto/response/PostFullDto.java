package ru.rakhmanov.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.rakhmanov.model.Tag;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostFullDto {
    private Integer id;
    private String title;
    private String content;
    private String imageUrl;
    private List<Tag> tags;
    private Long likesCount = 0L;
    private Long commentsCount = 0L;
}
