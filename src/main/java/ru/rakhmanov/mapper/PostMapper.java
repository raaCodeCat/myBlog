package ru.rakhmanov.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.rakhmanov.dto.response.PostResponseDto;
import ru.rakhmanov.model.Post;

import java.util.List;

//TODO перейти на mapstruct
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostMapper {
    public static List<PostResponseDto> mapToPostResponseDto(List<Post> posts) {
        return posts.stream()
                .map(PostMapper::mapToPostResponseDto)
                .toList();
    }

    public static PostResponseDto mapToPostResponseDto(Post post) {
        PostResponseDto postResponseDto = new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                post.getTags(),
                0L,
                0L
        );

        return postResponseDto;
    }
}
