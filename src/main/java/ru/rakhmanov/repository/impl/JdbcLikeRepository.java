package ru.rakhmanov.repository.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.rakhmanov.repository.LikeRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JdbcLikeRepository implements LikeRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLikeToPost(Integer postId) {
        String sql = "insert into postlikes (post_id) values (?)";
        jdbcTemplate.update(sql, postId);
    }

    @Override
    public Integer getLikesCountByPostId(Integer postId) {
        String sql = "select count(*) from postlikes where post_id = ?";

        return jdbcTemplate.queryForObject(sql, Integer.class, postId);
    }

    @Override
    public Map<Integer, Integer> getLikesCountByPostIds(List<Integer> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return Map.of();
        }

        String ids = StringUtils.join(postIds, ",");
        String sql = "select post_id, count(*) like_count from postlikes where post_id in " + "(" + ids + ") group by post_id";

        List<PostLikeCount> postCommentCounts = jdbcTemplate.query(sql, postLikeCountRowMapper());

        return postCommentCounts.stream()
                .collect(Collectors.toMap(PostLikeCount::getPostId, PostLikeCount::getLikeCount));
    }

    public static RowMapper<PostLikeCount> postLikeCountRowMapper() {
        return (rs, rowNum) -> new PostLikeCount(
                rs.getInt("post_id"),
                rs.getInt("like_count")
        );
    }

    @Getter
    @AllArgsConstructor
    public static class PostLikeCount {
        private Integer postId;
        private Integer likeCount;
    }
}
