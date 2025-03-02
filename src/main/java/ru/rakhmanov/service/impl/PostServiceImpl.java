package ru.rakhmanov.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rakhmanov.dto.response.PostFullDto;
import ru.rakhmanov.mapper.PostMapper;
import ru.rakhmanov.model.Post;
import ru.rakhmanov.model.Tag;
import ru.rakhmanov.repository.PostRepository;
import ru.rakhmanov.service.PostService;
import ru.rakhmanov.service.TagService;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final TagService tagService;

    @Override
    public List<PostFullDto> getAllPosts() {
        List<Post> posts = postRepository.findAllPosts();
        List<Integer> postIds = posts.stream()
                .map(Post::getId)
                .toList();

        List<PostFullDto> postFullDtos = PostMapper.mapToPostFullDto(posts);
        Map<Integer, List<Tag>> tagsByPostIds = tagService.getTagsByPostIds(postIds);

        enrichPostsByTags(postFullDtos, tagsByPostIds);

        return postFullDtos;
    }

    private void enrichPostsByTags (List<PostFullDto> postFullDtos, Map<Integer, List<Tag>> tagsByPostIds) {
        postFullDtos.forEach(post -> post.setTags(tagsByPostIds.getOrDefault(post.getId(), List.of())));
    }
}
