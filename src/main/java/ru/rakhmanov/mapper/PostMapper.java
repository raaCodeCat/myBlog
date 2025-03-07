package ru.rakhmanov.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import ru.rakhmanov.dto.response.PostFullDto;
import ru.rakhmanov.model.Post;

import java.util.List;

@Component
public class PostMapper {
    public List<PostFullDto> mapToPostFullDto(List<Post> posts) {
        return posts.stream()
                .map(this::mapToPostFullDto)
                .toList();
    }

    public PostFullDto mapToPostFullDto(Post post) {
        PostFullDto postFullDto = new PostFullDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                List.of(),
                0,
                0
        );

        return postFullDto;
    }
}
