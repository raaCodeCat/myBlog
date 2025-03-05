package ru.rakhmanov.service;

import java.util.List;
import java.util.Map;

public interface LikeService {

    Integer getLikesCountByPostId(Integer postId);

    Map<Integer, Integer> getLikesCountByPostIds(List<Integer> postIds);

}
