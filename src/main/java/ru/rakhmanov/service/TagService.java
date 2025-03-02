package ru.rakhmanov.service;

import ru.rakhmanov.model.Tag;

import java.util.List;
import java.util.Map;

public interface TagService {

    List<Tag> getAllTags();

    List<Tag> getTagsByPostId(Integer postId);

    Map<Integer, List<Tag>> getTagsByPostIds(List<Integer> postIds);

}
