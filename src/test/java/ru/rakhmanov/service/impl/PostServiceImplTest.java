package ru.rakhmanov.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.rakhmanov.dto.response.PostFullDto;
import ru.rakhmanov.mapper.PostMapper;
import ru.rakhmanov.model.Post;
import ru.rakhmanov.model.Tag;
import ru.rakhmanov.repository.LikeRepository;
import ru.rakhmanov.repository.PostRepository;
import ru.rakhmanov.repository.TagRepository;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
@ContextConfiguration(classes = PostServiceImpl.class)
class PostServiceImplTest {

    @MockitoBean
    private PostRepository postRepository;

    @MockitoBean
    private TagRepository tagRepository;

    @MockitoBean
    private LikeRepository likeRepository;

    @MockitoBean
    private PostMapper postMapper;

    @Autowired
    private PostServiceImpl postService;

    private Post post;
    private PostFullDto postFullDto;
    private List<Tag> tags;
    private List<Integer> tagIds;

    @BeforeEach
    void setUp() {
        post = new Post();
        post.setId(1);
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setImageUrl("test.jpg");
        post.setLikesCount(10);
        post.setCommentsCount(5);

        postFullDto = new PostFullDto();
        postFullDto.setId(1);
        postFullDto.setTitle("Test Post");
        postFullDto.setContent("Test Content");
        postFullDto.setImageUrl("test.jpg");
        postFullDto.setLikesCount(10);
        postFullDto.setCommentsCount(5);

        tags = List.of(new Tag(1, "Tag1"), new Tag(2, "Tag2"));
        tagIds = List.of(1, 2);
    }

    @Test
    void getAllPosts_ShouldReturnListOfPosts() {
        when(postRepository.findAllPosts(null, 0, 10)).thenReturn(List.of(post));
        when(postMapper.mapToPostFullDto(List.of(post))).thenReturn(List.of(postFullDto));
        when(tagRepository.findTagsByPostIds(List.of(1))).thenReturn(Map.of(1, tags));

        List<PostFullDto> actual = postService.getAllPosts(null, 0, 10);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals("Test Post", actual.get(0).getTitle());
        assertEquals(2, actual.get(0).getTags().size());
        assertEquals(5, actual.get(0).getCommentsCount());
        assertEquals(10, actual.get(0).getLikesCount());

        verify(postRepository, times(1)).findAllPosts(null, 0, 10);
        verify(tagRepository, times(1)).findTagsByPostIds(List.of(1));
    }

    @Test
    void getPostById_ShouldReturnPost() {
        when(postRepository.findPostById(1)).thenReturn(post);
        when(postMapper.mapToPostFullDto(post)).thenReturn(postFullDto);
        when(tagRepository.findTagsByPostId(1)).thenReturn(tags);

        PostFullDto actual = postService.getPostById(1);

        assertNotNull(actual);
        assertEquals("Test Post", actual.getTitle());
        assertEquals(2, actual.getTags().size());

        verify(postRepository, times(1)).findPostById(1);
        verify(tagRepository, times(1)).findTagsByPostId(1);
    }

    @Test
    void createPost_ShouldSavePostAndTags() {
        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("Test Content");
        post.setImageUrl("test.jpg");

        when(postRepository.savePost(post)).thenReturn(1);

        postService.createPost("Test Post", "test.jpg", "Test Content", tagIds);

        verify(postRepository, times(1)).savePost(post);
        verify(tagRepository, times(1)).saveTagsToPost(1, tagIds);
    }

    @Test
    void editPost_ShouldUpdatePostAndTags() {
        postService.editPost(1, "Updated Title", "updated.jpg", "Updated Content", tagIds);

        verify(postRepository, times(1)).updatePost(1, "Updated Title", "updated.jpg", "Updated Content");
        verify(tagRepository, times(1)).deleteTagsFromPost(1);
        verify(tagRepository, times(1)).saveTagsToPost(1, tagIds);
    }

    @Test
    void deletePostById_ShouldDeletePost() {
        postService.deletePostById(1);

        verify(postRepository, times(1)).deletePostById(1);
    }

    @Test
    void likePost_ShouldAddLike() {
        postService.likePost(1);

        verify(likeRepository, times(1)).addLikeToPost(1);
    }
    
    @Test
    void getPostCount_ShouldReturnCount() {
        when(postRepository.countPosts(anyInt())).thenReturn(10);
        
        Integer actual = postService.getPostCount(anyInt());

        assertEquals(10, actual);
        
        verify(postRepository, times(1)).countPosts(anyInt());
    }
}