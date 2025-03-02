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
    public List<PostFullDto> getAllPosts(Integer tagId, Integer page, Integer size) {
        List<Post> posts = postRepository.findAllPosts(tagId, page, size);
        List<Integer> postIds = posts.stream()
                .map(Post::getId)
                .toList();

        List<PostFullDto> postFullDtos = PostMapper.mapToPostFullDto(posts);
        Map<Integer, List<Tag>> tagsByPostIds = tagService.getTagsByPostIds(postIds);

        enrichPostsByTags(postFullDtos, tagsByPostIds);

        return postFullDtos;
    }

    @Override
    public PostFullDto getPostById(Integer id) {
        Post post = postRepository.findPostById(id);
        PostFullDto postFullDto = PostMapper.mapToPostFullDto(post);
        List<Tag> tags = tagService.getTagsByPostId(id);
        postFullDto.setTags(tags);

        return postFullDto;
    }

    @Override
    public void deletePostById(Integer id) {
        postRepository.deletePostById(id);
    }

    @Override
    public Integer getPostCount(Integer tagId) {
        return postRepository.countPosts(tagId);
    }

    private void enrichPostsByTags (List<PostFullDto> postFullDtos, Map<Integer, List<Tag>> tagsByPostIds) {
        postFullDtos.forEach(post -> post.setTags(tagsByPostIds.getOrDefault(post.getId(), List.of())));
    }
}
