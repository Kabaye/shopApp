package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.entity.Tag;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
public interface TagService {
    Tag saveTag(Tag tag);

    Tag getTagById(Long id);

    Tag getTagByName(String tagname);

    Set<Tag> saveTags(Set<Tag> tags);

    Tag updateTag(Tag tag);

    void deleteTagById(Long id);
}
