package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.dao.TagDAO;
import com.netcracker.edu.kulich.entity.Tag;
import com.netcracker.edu.kulich.service.exception.TagServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service(value = "tagService")
public class DefaultTagService implements TagService {
    private final String NULL_TAG_NAME_EXCEPTION_MESSAGE = "Tag name is empty, please set it not empty.";
    private final String DELETING_NOT_EXISTENT_TAG = "You try to delete not existent tag.";
    private final String INSERTING_OR_UPDATING_TAG_WITH_NOT_UNIC_NAME = "You try to insert / update tag with already existent name, please, set name unique.";

    @Autowired
    private TagDAO tagDAO;

    @Override
    public Tag saveTag(Tag tag) throws TagServiceException {
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
    public Set<Tag> saveTags(Set<Tag> tags) throws TagServiceException {
        tags = tags.stream().filter(tag -> tag != null).collect(Collectors.toSet());
        for (Tag tag : tags) {
            checkTagHaveNotNullNameAndUnique(tag);
        }
        tagDAO.create(tags);
        return tags;
    }

    @Override
    public Tag updateTag(Tag tag) throws TagServiceException {
        checkTagHaveNotNullNameAndUnique(tag);
        tag = tagDAO.update(tag);
        return tag;
    }

    @Override
    public void deleteTagById(Long id) throws TagServiceException {
        try {
            tagDAO.delete(id);
        } catch (EntityNotFoundException exc) {
            throw new TagServiceException(DELETING_NOT_EXISTENT_TAG);
        }
    }

    private void checkTagHaveNotNullNameAndUnique(Tag tag) throws TagServiceException {
        if (tag.getTagname().equals(""))
            throw new TagServiceException(NULL_TAG_NAME_EXCEPTION_MESSAGE);
        Tag tag1 = tagDAO.readByName(tag.getTagname());
        if (tag1 != null) {
            throw new TagServiceException(INSERTING_OR_UPDATING_TAG_WITH_NOT_UNIC_NAME);
        }
    }
}
