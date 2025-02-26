package ru.rakhmanov.service;

import ru.rakhmanov.dto.response.PostResponseDto;

import java.util.List;

public interface PostService {
    List<PostResponseDto> getAllPosts();
}
