package ru.rakhmanov.repository.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.rakhmanov.model.Post;
import ru.rakhmanov.repository.PostRepository;

import java.util.List;

@Repository
@AllArgsConstructor
public class JdbcPostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Post> findAllPosts(Integer tagId, Integer page, Integer size) {
        String offsetLimit = "";
        String joinString = "";
        String whereString = "";

        if (tagId != null) {
            joinString += "join posttags pt on p.post_id = pt.post_id ";
            whereString += "and pt.tag_id = " + tagId + " ";
        }

        if (page != null && size != null) {
            int offset = page * size;
            offsetLimit = "limit " + size + " offset " + offset;
        }

        String sql = "select p.post_id, p.post_title, p.post_content, p.post_image_url from posts p " +
                joinString +
                "where (1 =  1) " +
                whereString + offsetLimit;

        return jdbcTemplate.query(sql, postRowMapper());
    }

    @Override
    public Post findPostById(Integer id) {
        String sql = "select post_id, post_title, post_content, post_image_url from posts where post_id = ?";
        return jdbcTemplate.queryForObject(sql, postRowMapper(), id);
    }

    @Override
    public void deletePostById(Integer id) {
        String sql = "delete from posts where post_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Integer countPosts(Integer tagId) {
        String tagOption = "";

        if (tagId != null) {
            tagOption += " join posttags pt on p.post_id = pt.post_id where pt.tag_id = " + tagId;
        }

        String sql = "select count(*) from posts p" + tagOption;
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    private static RowMapper<Post> postRowMapper() {
        return (rs, rowNum) -> new Post(
                rs.getInt("post_id"),
                rs.getString("post_title"),
                rs.getString("post_content"),
                rs.getString("post_image_url")
        );
    }
}
