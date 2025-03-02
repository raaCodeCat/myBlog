package ru.rakhmanov.service;

import org.springframework.data.relational.core.sql.In;
import ru.rakhmanov.dto.response.PostFullDto;

import java.util.List;

public interface PostService {

    List<PostFullDto> getAllPosts(Integer tagId, Integer page, Integer size);

    PostFullDto getPostById(Integer id);

    void deletePostById(Integer id);

    Integer getPostCount(Integer tagId);

}
