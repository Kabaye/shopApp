package com.netcracker.edu.kulich.tag.service;

import com.netcracker.edu.kulich.tag.entity.Tag;

import java.util.Set;

public interface TagService {
    Tag saveTag(Tag tag);

    Tag getTagById(Long id);

    Tag getTagByName(String tagname);

    Set<Tag> saveTags(Set<Tag> tags);

    Tag updateTag(Tag tag);

    void deleteTagById(Long id);
}
