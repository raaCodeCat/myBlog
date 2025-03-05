package ru.rakhmanov.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rakhmanov.repository.LikeRepository;
import ru.rakhmanov.service.LikeService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    @Override
    public Integer getLikesCountByPostId(Integer postId) {
        return likeRepository.getLikesCountByPostId(postId);
    }

    @Override
    public Map<Integer, Integer> getLikesCountByPostIds(List<Integer> postIds) {
        return likeRepository.getLikesCountByPostIds(postIds);
    }
}
