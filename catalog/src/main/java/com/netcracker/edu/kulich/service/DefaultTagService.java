package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.dao.TagDAO;
import com.netcracker.edu.kulich.entity.Tag;
import com.netcracker.edu.kulich.exception.service.ServiceException;
import com.netcracker.edu.kulich.service.validation.NameValidator;
import com.netcracker.edu.kulich.service.validation.TagValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service(value = "tagService")
public class DefaultTagService implements TagService {

    private TagDAO tagDAO;
    private TagValidator tagValidator;
    private NameValidator tagNameValidator;

    public DefaultTagService(TagDAO tagDAO, TagValidator tagValidator, NameValidator tagNameValidator) {
        this.tagDAO = tagDAO;
        this.tagValidator = tagValidator;
        this.tagNameValidator = tagNameValidator;
    }

    @Override
    public Tag saveTag(Tag tag) {
        tag.fixTagName();
        tagValidator.checkForPersist(tag);
        Tag findedTag = tagDAO.readByName(tag.getTagname());
        if (findedTag != null) {
            return findedTag;
        }
        tag = tagDAO.create(tag);
        return tag;
    }

    @Override
    public Tag getTagById(Long id) {
        tagValidator.checkIdIsNotNull(id);
        return tagDAO.read(id);
    }

    @Override
    public Tag getTagByName(String tagname) {
        tagNameValidator.check(tagname);
        return tagDAO.readByName(tagname);
    }

    @Override
    public Set<Tag> saveTags(Set<Tag> tags) {
        tags = tags.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        tags = tags.stream()
                .map(this::saveTag)
                .collect(Collectors.toSet());
        return tags;
    }

    @Override
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
    public void deleteTagById(Long id) {
        tagValidator.checkIdIsNotNull(id);
        try {
            tagDAO.delete(id);
        } catch (EntityNotFoundException exc) {
            throw new ServiceException("Tag with id: \"" + id + "\" doesn't exist. You can't delete not existent tag.");
        }
    }
}
