package com.netcracker.edu.kulich.tag.validation;

import com.netcracker.edu.kulich.exception.service.ServiceException;
import com.netcracker.edu.kulich.tag.entity.Tag;
import com.netcracker.edu.kulich.validation.NameValidator;
import com.netcracker.edu.kulich.validation.ServiceValidator;
import com.netcracker.edu.kulich.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Validator
public class TagValidator implements ServiceValidator<Tag, Long> {
    private static final Logger logger = LoggerFactory.getLogger(ServiceValidator.class);
    private static final String ID_NOT_NULL = "Tag id must be initialized.";
    private static final String ID_NULL = "Tag id must not be initialized.";
    private static final String TAG_DOESNT_EXIST = "Tag with such id doesn't exist. Please, set id valid.";
    private static final String TAG_NOT_NULL = "Tag can't be null.";

    private NameValidator tagNameValidator;

    public TagValidator(NameValidator tagNameValidator) {
        this.tagNameValidator = tagNameValidator;
    }

    @Override
    public Long extractId(Tag resource) {
        return resource.getId();
    }

    @Override
    public void checkIdIsNull(Long id) {
        if (id != 0L) {
            logger.error("Id of tag is not NULL");
            throw new ServiceException(ID_NULL);
        }
    }

    @Override
    public void checkIdIsNotNull(Long id) {
        if (id == 0L) {
            logger.error("Id of tag is NULL");
            throw new ServiceException(ID_NOT_NULL);
        }
    }

    @Override
    public void checkNotNull(Tag resource) {
        if (resource == null) {
            logger.error("Tag is NULL");
            throw new ServiceException(TAG_NOT_NULL);
        }
    }

    @Override
    public void checkNotNull(Tag resource, String errorMessage) {
        if (resource == null) {
            logger.error("Tag is NULL");
            throw new ServiceException(errorMessage);
        }
    }

    @Override
    public void checkFoundById(Tag resource) {
        checkNotNull(resource, TAG_DOESNT_EXIST);
    }

    public void checkFoundByName(Tag resource, String name) {
        checkNotNull(resource, "Tag with name: \"" + name + "\" doesn't exist. Please, firstly, create tag and then, create offer with such tag.");
    }

    @Override
    public void checkProperties(Tag resource) {
        tagNameValidator.check(resource.getTagname());
    }
}
