package ru.rakhmanov.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rakhmanov.repository.LikeRepository;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeServiceImplTest {

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private LikeServiceImpl likeService;

    private List<Integer> postIds;
    private Map<Integer, Integer> likesCountMap;

    @BeforeEach
    void setUp() {
        postIds = List.of(1, 2, 3);
        likesCountMap = Map.of(1, 10, 2, 5, 3, 8);
    }

    @Test
    void getLikesCountByPostId_ShouldReturnLikesCount() {
        when(likeRepository.getLikesCountByPostId(1)).thenReturn(10);

        Integer actual = likeService.getLikesCountByPostId(1);

        assertNotNull(actual);
        assertEquals(10, actual);

        verify(likeRepository, times(1)).getLikesCountByPostId(1);
    }

    @Test
    void getLikesCountByPostIds_ShouldReturnLikesCountMap() {
        when(likeRepository.getLikesCountByPostIds(postIds)).thenReturn(likesCountMap);

        Map<Integer, Integer> actual = likeService.getLikesCountByPostIds(postIds);

        assertNotNull(actual);
        assertEquals(3, actual.size());
        assertEquals(10, actual.get(1));
        assertEquals(5, actual.get(2));
        assertEquals(8, actual.get(3));

        verify(likeRepository, times(1)).getLikesCountByPostIds(postIds);
    }

    @Test
    void getLikesCountByPostId_ShouldReturnZeroWhenNoLikes() {
        when(likeRepository.getLikesCountByPostId(1)).thenReturn(0);

        Integer actual = likeService.getLikesCountByPostId(1);

        assertNotNull(actual);
        assertEquals(0, actual);

        verify(likeRepository, times(1)).getLikesCountByPostId(1);
    }

    @Test
    void getLikesCountByPostIds_ShouldReturnEmptyMapForEmptyPostIds() {
        when(likeRepository.getLikesCountByPostIds(List.of())).thenReturn(Map.of());

        Map<Integer, Integer> actual = likeService.getLikesCountByPostIds(List.of());

        assertNotNull(actual);
        assertTrue(actual.isEmpty());

        verify(likeRepository, times(1)).getLikesCountByPostIds(List.of());
    }
}