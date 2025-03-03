package ru.rakhmanov.repository.impl;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.rakhmanov.model.PostTag;
import ru.rakhmanov.model.Tag;
import ru.rakhmanov.repository.TagRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class JdbcTagRepository implements TagRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Tag> findAllTags() {
        String sql = "select tag_id, tag_name from tags";
        return jdbcTemplate.query(sql, tagRowMapper());
    }

    @Override
    public List<Tag> findTagsByPostId(Integer postId) {
        if (postId == null) {
            return List.of();
        }

        String sql = """
                select t.tag_id, t.tag_name 
                from tags t
                inner join posttags pt on pt.tag_id = t.tag_id
                where pt.post_id = ?
                """;
        return jdbcTemplate.query(sql, tagRowMapper(), postId);
    }

    @Override
    public Map<Integer, List<Tag>> findTagsByPostId(List<Integer> postIds) {
        if (postIds == null || postIds.isEmpty()) {
            return Map.of();
        }

        String ids = StringUtils.join(postIds, ",");
        String sql = """
                select pt.post_id, t.tag_id, t.tag_name
                from tags t
                inner join posttags pt on pt.tag_id = t.tag_id
                where pt.post_id in (""" + ids + ")";

        List<PostTag> tags = jdbcTemplate.query(sql, postTagRowMapper());

        return tags.stream()
                .collect(Collectors.groupingBy(
                        PostTag::getPostId,
                        Collectors.mapping(
                                postTag -> new Tag(postTag.getTagId(), postTag.getTagName()),
                                Collectors.toList()
                        )
                ));
    }

    @Override
    public void saveTagsToPost(Integer postId, List<Integer> tagIds) {
        String sqlValues = tagIds.stream()
                .map(tag -> "(" + postId + "," + tag + ")")
                .collect(Collectors.joining(","));
        String sql = "insert into posttags (post_id, tag_id) values " + sqlValues;

        jdbcTemplate.update(sql);
    }

    private static RowMapper<Tag> tagRowMapper() {
        return (rs, rowNum) -> new Tag (
                rs.getInt("tag_id"),
                rs.getString("tag_name")
        );
    }

    private static RowMapper<PostTag> postTagRowMapper() {
        return (rs, rowNum) -> new PostTag (
                rs.getInt("post_id"),
                rs.getInt("tag_id"),
                rs.getString("tag_name")
        );
    }
}
