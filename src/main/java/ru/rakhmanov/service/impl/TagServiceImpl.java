package ru.rakhmanov.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rakhmanov.model.Tag;
import ru.rakhmanov.repository.TagRepository;
import ru.rakhmanov.service.TagService;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAllTags();
    }

    @Override
    public Map<Integer, List<Tag>> getTagsByPostIds(List<Integer> postIds) {
        return tagRepository.findTagsByPostId(postIds);
    }

}
