package com.netcracker.edu.kulich.offer.validation;

import com.netcracker.edu.kulich.category.entity.Category;
import com.netcracker.edu.kulich.exception.service.ServiceException;
import com.netcracker.edu.kulich.offer.entity.Offer;
import com.netcracker.edu.kulich.tag.entity.Tag;
import com.netcracker.edu.kulich.validation.NameValidator;
import com.netcracker.edu.kulich.validation.PriceValidator;
import com.netcracker.edu.kulich.validation.ServiceValidator;
import com.netcracker.edu.kulich.validation.Validator;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Validator
public class OfferValidator implements ServiceValidator<Offer, Long> {
    private static final Logger logger = LoggerFactory.getLogger(ServiceValidator.class);
    private static final String ID_NOT_NULL = "Offer id must be initialized.";
    private static final String ID_NULL = "Offer id must not be initialized.";
    private static final String OFFER_DOESNT_EXIST = "Offer with such id doesn't exist. Please, set id valid.";
    private static final String OFFER_NOT_NULL = "Offer can't be null.";
    private final NameValidator offerNameValidator;
    private final PriceValidator offerPriceValidator;
    private final ServiceValidator<Category, Long> categoryValidator;
    private final ServiceValidator<Tag, Long> tagValidator;

    public OfferValidator(NameValidator offerNameValidator, PriceValidator offerPriceValidator, ServiceValidator<Category, Long> categoryValidator, ServiceValidator<Tag, Long> tagValidator) {
        this.offerNameValidator = offerNameValidator;
        this.offerPriceValidator = offerPriceValidator;
        this.categoryValidator = categoryValidator;
        this.tagValidator = tagValidator;
    }

    @Override
    public Long extractId(Offer resource) {
        return resource.getId();
    }

    @Override
    public void checkIdIsNull(Long id) {
        if (id != 0L) {
            logger.error("Id of offer is not NULL.");
            throw new ServiceException(ID_NULL);
        }
    }

    @Override
    public void checkIdIsNotNull(Long id) {
        if (id == 0L) {
            logger.error("Id of offer is NULL.");
            throw new ServiceException(ID_NOT_NULL);
        }
    }

    @Override
    public void checkNotNull(Offer resource) {
        if (resource == null) {
            logger.error("Offer is NULL.");
            throw new ServiceException(OFFER_NOT_NULL);
        }
    }

    @Override
    public void checkNotNull(Offer resource, String errorMessage) {
        if (resource == null) {
            logger.error("Offer is NULL.");
            throw new ServiceException(errorMessage);
        }
    }

    @Override
    public void checkFoundById(Offer resource) {
        checkNotNull(resource, OFFER_DOESNT_EXIST);
    }

    @Override
    public void checkProperties(Offer resource) {
        offerNameValidator.check(resource.getName());
        offerPriceValidator.check(resource.getPrice());
        categoryValidator.checkProperties(resource.getCategory());
        resource.getTags().forEach(tagValidator::checkProperties);
    }
}
