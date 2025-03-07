package ru.rakhmanov.repository.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.rakhmanov.model.Comment;
import ru.rakhmanov.repository.CommentRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcCommentRepository implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveComment(Integer postId, String commentText) {
        String sql = "insert into comments (post_id, comment_content) values (?, ?)";
        jdbcTemplate.update(sql, postId, commentText);
    }

    @Override
    public List<Comment> getCommentsByPostId(Integer postId) {
        String sql = "select comment_id, post_id, comment_content from comments where post_id = ?";

        return jdbcTemplate.query(sql, commentRowMapper(), postId);
    }

    @Override
    public Map<Integer, Integer> getCommentsCountByPostIds(List<Integer> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return Map.of();
        }

        String ids = StringUtils.join(postIds, ",");
        String sql = "select post_id, count(comment_id) comment_count from comments where post_id in " + "(" + ids + ") group by post_id";

        List<PostCommentCount> postCommentCounts = jdbcTemplate.query(sql, postCommentCountRowMapper());

        return postCommentCounts.stream()
                .collect(Collectors.toMap(PostCommentCount::getPostId, PostCommentCount::getCommentCount));
    }

    @Override
    public void editComment(Integer commentId, String commentText) {
        String sql = "update comments set comment_content = ? where comment_id = ?";
        jdbcTemplate.update(sql, commentText, commentId);
    }

    @Override
    public void deleteComment(Integer commentId) {
        String sql = "delete from comments where comment_id = ?";
        jdbcTemplate.update(sql, commentId);
    }

    private static RowMapper<Comment> commentRowMapper() {
        return (rs, rowNum) -> new Comment(
                rs.getInt("comment_id"),
                rs.getInt("post_id"),
                rs.getString("comment_content")
        );
    }

    private static RowMapper<PostCommentCount> postCommentCountRowMapper() {
        return (rs, rowNum) -> new PostCommentCount(
                rs.getInt("post_id"),
                rs.getInt("comment_count")
        );
    }

    @Getter
    @AllArgsConstructor
    private static class PostCommentCount {
        private Integer postId;
        private Integer commentCount;
    }
}
