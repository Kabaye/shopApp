package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.dao.TagDAO;
import com.netcracker.edu.kulich.entity.Tag;
import com.netcracker.edu.kulich.exception.service.TagServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service(value = "tagService")
public class DefaultTagService implements TagService {
    private static final String NULL_TAG_NAME_EXCEPTION_MESSAGE = "Tag name is empty, please, set it not empty.";
    private static final String DELETING_OR_UPDATING_NOT_EXISTENT_TAG = "You try to delete / update not existent tag.";
    private static final String INSERTING_OR_UPDATING_TAG_WITH_NOT_UNIQUE_NAME = "You try to insert / update tag with already existent name, please, set name unique.";

    @Autowired
    private TagDAO tagDAO;

    @Override
    public Tag saveTag(Tag tag) {
        checkTagHaveNotNullNameAndUnique(tag);
        tagDAO.create(tag);
        return tag;
    }

    @Override
    public Tag getTagById(Long id) {
        return tagDAO.read(id);
    }

    @Override
    public Tag getTagByName(String tagname) {
        return tagDAO.readByName(tagname);
    }

    @Override
    public Set<Tag> saveTags(Set<Tag> tags) {
        tags = tags.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        for (Tag tag : tags) {
            checkTagHaveNotNullNameAndUnique(tag);
        }
        tagDAO.create(tags);
        return tags;
    }

    @Override
    public Tag updateTag(Tag tag) {
        checkTagHaveNotNullNameAndUnique(tag);
        Tag tag1 = tagDAO.read(tag.getId());
        if (tag1 == null) {
            throw new TagServiceException(DELETING_OR_UPDATING_NOT_EXISTENT_TAG);
        }
        tag.setOffers(tag1.getOffers());
        tag = tagDAO.update(tag);
        return tag;
    }

    @Override
    public void deleteTagById(Long id) {
        try {
            tagDAO.delete(id);
        } catch (EntityNotFoundException exc) {
            throw new TagServiceException(DELETING_OR_UPDATING_NOT_EXISTENT_TAG);
        }
    }

    private void checkTagHaveNotNullNameAndUnique(Tag tag) {
        if (tag.getTagname().equals("")) {
            throw new TagServiceException(NULL_TAG_NAME_EXCEPTION_MESSAGE);
        }
        Tag tag1 = tagDAO.readByName(tag.getTagname());
        if (tag1 != null) {
            throw new TagServiceException(INSERTING_OR_UPDATING_TAG_WITH_NOT_UNIQUE_NAME);
        }
    }
}
