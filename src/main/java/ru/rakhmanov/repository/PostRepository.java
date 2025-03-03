package ru.rakhmanov.repository;

import ru.rakhmanov.model.Post;

import java.util.List;

public interface PostRepository {

    List<Post> findAllPosts(Integer tagId, Integer page, Integer size);

    Post findPostById(Integer id);

    void deletePostById(Integer id);

    Integer countPosts(Integer tagId);

    Integer savePost(Post post);

}
