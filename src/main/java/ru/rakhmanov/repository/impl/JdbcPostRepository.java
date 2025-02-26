package ru.rakhmanov.repository.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.rakhmanov.model.Post;
import ru.rakhmanov.repository.PostRepository;

import java.util.List;

@Repository
@AllArgsConstructor
public class JdbcPostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Post> findAllPosts() {
        return jdbcTemplate.query("select post_id, post_title, post_content, post_image_url from posts",
            (rs, rowNum) -> new Post(
                    rs.getInt("post_id"),
                    rs.getString("post_title"),
                    rs.getString("post_content"),
                    rs.getString("post_image_url"),
                    List.of()
            )
        );
    }
}
