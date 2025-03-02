package ru.rakhmanov.repository;

import ru.rakhmanov.model.Tag;

import java.util.List;
import java.util.Map;

public interface TagRepository {

    List<Tag> findAllTags();

    List<Tag> findTagsByPostId(Integer postId);

    Map<Integer, List<Tag>> findTagsByPostId(List<Integer> postIds);

}
