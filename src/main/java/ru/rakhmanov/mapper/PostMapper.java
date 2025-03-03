package ru.rakhmanov.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.rakhmanov.dto.response.PostFullDto;
import ru.rakhmanov.model.Post;

import java.util.List;

//TODO перейти на mapstruct
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostMapper {
    public static List<PostFullDto> mapToPostFullDto(List<Post> posts) {
        return posts.stream()
                .map(PostMapper::mapToPostFullDto)
                .toList();
    }

    public static PostFullDto mapToPostFullDto(Post post) {
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
