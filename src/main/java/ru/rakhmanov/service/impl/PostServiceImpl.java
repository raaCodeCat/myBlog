package ru.rakhmanov.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rakhmanov.dto.response.PostResponseDto;
import ru.rakhmanov.mapper.PostMapper;
import ru.rakhmanov.model.Post;
import ru.rakhmanov.repository.PostRepository;
import ru.rakhmanov.service.PostService;

import java.util.List;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    public List<PostResponseDto> getAllPosts() {
        List<Post> posts = postRepository.findAllPosts();

        return PostMapper.mapToPostResponseDto(posts);
    }
}
