package com.netcracker.edu.kulich.service.validation;

import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Tag;
import com.netcracker.edu.kulich.exception.service.ServiceException;
import lombok.Getter;

@Getter
@Validator
public class OfferValidator implements ServiceValidator<Offer, Long> {
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
            throw new ServiceException(ID_NULL);
        }
    }

    @Override
    public void checkIdIsNotNull(Long id) {
        if (id == 0L) {
            throw new ServiceException(ID_NOT_NULL);
        }
    }

    @Override
    public void checkNotNull(Offer resource) {
        if (resource == null) {
            throw new ServiceException(OFFER_NOT_NULL);
        }
    }

    @Override
    public void checkNotNull(Offer resource, String errorMessage) {
        if (resource == null) {
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