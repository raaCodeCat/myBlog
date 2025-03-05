package ru.rakhmanov.repository;

import java.util.List;
import java.util.Map;

public interface LikeRepository {

    void addLikeToPost(Integer postId);

    Integer getLikesCountByPostId(Integer postId);

    Map<Integer, Integer> getLikesCountByPostIds(List<Integer> postIds);

}
