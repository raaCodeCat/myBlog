package ru.rakhmanov.repository;

import ru.rakhmanov.model.Post;

import java.util.List;

public interface PostRepository {

    List<Post> findAllPosts();

    Post findPostById(Integer id);
}
