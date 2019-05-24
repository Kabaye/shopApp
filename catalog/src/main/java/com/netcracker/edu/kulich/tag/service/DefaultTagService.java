package com.netcracker.edu.kulich.tag.service;

import com.netcracker.edu.kulich.exception.service.ServiceException;
import com.netcracker.edu.kulich.logging.annotation.DefaultLogging;
import com.netcracker.edu.kulich.logging.annotation.Logging;
import com.netcracker.edu.kulich.tag.dao.TagDAO;
import com.netcracker.edu.kulich.tag.entity.Tag;
import com.netcracker.edu.kulich.tag.validation.TagValidator;
import com.netcracker.edu.kulich.validation.NameValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service(value = "tagService")
@DefaultLogging
public class DefaultTagService implements TagService {
    private static final Logger logger = LoggerFactory.getLogger(TagService.class);
    private TagDAO tagDAO;
    private TagValidator tagValidator;
    private NameValidator tagNameValidator;

    public DefaultTagService(TagDAO tagDAO, TagValidator tagValidator, NameValidator tagNameValidator) {
        this.tagDAO = tagDAO;
        this.tagValidator = tagValidator;
        this.tagNameValidator = tagNameValidator;
    }

    @Override
    @Logging(startMessage = "Request on saving tag to database is received.", endMessage = "Tag is successfully saved to database.")
    public Tag saveTag(Tag tag) {
        tag.fixTagName();
        tagValidator.checkForPersist(tag);
        Tag foundedTag = tagDAO.readByName(tag.getTagname());
        if (foundedTag != null) {
            return foundedTag;
        }
        tag = tagDAO.create(tag);
        return tag;
    }

    @Override
    @Logging(startMessage = "Request on getting tag by id from database is received.", endMessage = "Tag is successfully get from database.")
    public Tag getTagById(Long id) {
        tagValidator.checkIdIsNotNull(id);
        return tagDAO.read(id);
    }

    @Override
    @Logging(startMessage = "Request on getting tag by name from database is received.", endMessage = "Tag is successfully get from database.")
    public Tag getTagByName(String tagname) {
        tagNameValidator.check(tagname);
        return tagDAO.readByName(tagname);
    }

    @Override
    @Logging(startMessage = "Request on saving some tags to database is received.", endMessage = "Tags are successfully saved to database.")
    public Set<Tag> saveTags(Set<Tag> tags) {
        tags = tags.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        tags = tags.stream()
                .map(this::saveTag)
                .collect(Collectors.toSet());
        return tags;
    }

    @Override
    @Logging(startMessage = "Request on updating tag in database is received.", endMessage = "Tag is successfully updated in database.")
    public Tag updateTag(Tag tag) {
        tag.fixTagName();
        tagValidator.checkForUpdate(tag);
        Tag foundedTag = tagDAO.read(tag.getId());
        tagValidator.checkFoundById(foundedTag);
        tag.setOffers(foundedTag.getOffers());
        tag = tagDAO.update(tag);
        return tag;
    }

    @Override
    @Logging(startMessage = "Request on deleting tag from database is received.", endMessage = "Tag is successfully deleted from database.")
    public void deleteTagById(Long id) {
        tagValidator.checkIdIsNotNull(id);
        try {
            tagDAO.delete(id);
        } catch (EntityNotFoundException exc) {
            logger.error("Attempt to delete not existent tag.");
            throw new ServiceException("Tag with id: \"" + id + "\" doesn't exist. You can't delete not existent tag.");
        }
    }
}
