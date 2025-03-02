package ru.rakhmanov.service;

import ru.rakhmanov.dto.response.PostFullDto;

import java.util.List;

public interface PostService {
    List<PostFullDto> getAllPosts();
}
