package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.entity.Tag;
import com.netcracker.edu.kulich.service.exception.TagServiceException;

import java.util.Set;

public interface TagService {
    Tag saveTag(Tag tag) throws TagServiceException;

    Tag getTagById(Long id);

    Tag getTagByName(String tagname);

    Set<Tag> saveTags(Set<Tag> tags) throws TagServiceException;

    Tag updateTag(Tag tag) throws TagServiceException;

    void deleteTagById(Long id) throws TagServiceException;
}
