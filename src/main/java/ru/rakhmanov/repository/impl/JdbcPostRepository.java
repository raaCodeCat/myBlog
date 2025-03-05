package ru.rakhmanov.repository.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.rakhmanov.model.Post;
import ru.rakhmanov.repository.PostRepository;

import java.sql.PreparedStatement;
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
                whereString + " order by p.post_id desc " + offsetLimit;

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

    @Override
    public Integer savePost(Post post) {
        String sql = "insert into posts(post_title, post_content, post_image_url) values(?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"post_id"});
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getContent());
            ps.setString(3, post.getImageUrl());
            return ps;
        }, keyHolder);

        return (Integer) (keyHolder.getKey());
    }

    @Override
    public void updatePost(Integer postId, String title, String imageUrl, String content) {
        String sql = "update posts set post_title = ?, post_content = ?, post_image_url = ? where post_id = ?";
        jdbcTemplate.update(sql, title, content, imageUrl, postId);
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
