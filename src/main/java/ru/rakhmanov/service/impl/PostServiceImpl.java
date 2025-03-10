package ru.rakhmanov.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rakhmanov.dto.response.PostFullDto;
import ru.rakhmanov.exception.NotFoundException;
import ru.rakhmanov.mapper.PostMapper;
import ru.rakhmanov.model.Post;
import ru.rakhmanov.model.Tag;
import ru.rakhmanov.repository.CommentRepository;
import ru.rakhmanov.repository.LikeRepository;
import ru.rakhmanov.repository.PostRepository;
import ru.rakhmanov.repository.TagRepository;
import ru.rakhmanov.service.PostService;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final PostMapper postMapper;

    @Override
    public List<PostFullDto> getAllPosts(Integer tagId, Integer page, Integer size) {
        List<Post> posts = postRepository.findAllPosts(tagId, page, size);
        List<Integer> postIds = posts.stream()
                .map(Post::getId)
                .toList();

        List<PostFullDto> postFullDtos = postMapper.mapToPostFullDto(posts);
        Map<Integer, List<Tag>> tagsByPostIds = tagRepository.findTagsByPostIds(postIds);
        Map<Integer, Integer> commentsCounts = commentRepository.getCommentsCountByPostIds(postIds);
        Map<Integer, Integer> likesCounts = likeRepository.getLikesCountByPostIds(postIds);

        enrichPostsByTags(postFullDtos, tagsByPostIds);
        enrichPostByComments(postFullDtos, commentsCounts);
        enrichPostByLikes(postFullDtos, likesCounts);

        return postFullDtos;
    }

    @Override
    public PostFullDto getPostById(Integer id) {
        Post post = postRepository.findPostById(id);

        if (post == null) {
            throw new NotFoundException();
        }

        PostFullDto postFullDto = postMapper.mapToPostFullDto(post);
        List<Tag> tags = tagRepository.findTagsByPostId(id);
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

    @Override
    @Transactional
    public void createPost(String title, String imageUrl, String content, List<Integer> tagIds) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setImageUrl(imageUrl);

        Integer postId = postRepository.savePost(post);

        saveTagsToPost(postId, tagIds);
    }

    @Override
    public void editPost(Integer postId, String title, String imageUrl, String content, List<Integer> tagIds) {
        postRepository.updatePost(postId, title, imageUrl, content);

        deleteTagsFromPost(postId);
        saveTagsToPost(postId, tagIds);
    }

    @Override
    @Transactional
    public void likePost(Integer postId) {
        likeRepository.addLikeToPost(postId);
    }

    private void enrichPostsByTags (List<PostFullDto> postFullDtos, Map<Integer, List<Tag>> tagsByPostIds) {
        postFullDtos.forEach(post -> post.setTags(tagsByPostIds.getOrDefault(post.getId(), List.of())));
    }

    private void enrichPostByComments(List<PostFullDto> postFullDtos, Map<Integer, Integer> commentsCounts) {
        postFullDtos.forEach(post -> post.setCommentsCount(commentsCounts.getOrDefault(post.getId(), 0)));
    }

    private void enrichPostByLikes(List<PostFullDto> postFullDtos, Map<Integer, Integer> likesCounts) {
        postFullDtos.forEach(post -> post.setLikesCount(likesCounts.getOrDefault(post.getId(), 0)));
    }

    private void saveTagsToPost(Integer postId, List<Integer> tagIds) {
        if (!(tagIds == null || tagIds.isEmpty())) {
            tagRepository.saveTagsToPost(postId, tagIds);
        }
    }

    private  void deleteTagsFromPost(Integer postId) {
        tagRepository.deleteTagsFromPost(postId);
    }
}
