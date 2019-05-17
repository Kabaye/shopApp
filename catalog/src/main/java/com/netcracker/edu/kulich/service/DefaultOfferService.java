package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.dao.OfferDAO;
import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Tag;
import com.netcracker.edu.kulich.exception.service.ServiceException;
import com.netcracker.edu.kulich.service.validation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service(value = "offerService")
public class DefaultOfferService implements OfferService {

    private OfferDAO offerDAO;
    private CategoryService categoryService;
    private TagService tagService;
    private OfferValidator offerValidator;
    private NameValidator offerNameValidator;
    private PriceValidator offerPriceValidator;
    private CategoryValidator categoryValidator;
    private TagValidator tagValidator;
    private PriceRangeValidator priceRangeValidator;

    public DefaultOfferService(OfferDAO offerDAO, CategoryService categoryService, TagService tagService, OfferValidator offerValidator,
                               NameValidator offerNameValidator, PriceValidator offerPriceValidator, CategoryValidator categoryValidator,
                               TagValidator tagValidator, PriceRangeValidator priceRangeValidator) {
        this.offerDAO = offerDAO;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.offerValidator = offerValidator;
        this.offerNameValidator = offerNameValidator;
        this.offerPriceValidator = offerPriceValidator;
        this.categoryValidator = categoryValidator;
        this.tagValidator = tagValidator;
        this.priceRangeValidator = priceRangeValidator;
    }

    @Override
    public Offer saveOffer(Offer offer) {
        offer.fixNames();
        offerValidator.checkForPersist(offer);
        recreateOfferCategoryAndTags(offer);
        offer = offerDAO.create(offer);
        return offer;
    }

    @Override
    public Offer getOfferById(Long id) {
        offerValidator.checkIdIsNotNull(id);
        return offerDAO.read(id);
    }

    @Override
    public Offer addTag(Long id, Tag tag) {
        offerValidator.checkIdIsNotNull(id);
        tagValidator.checkProperties(tag);
        Offer offer = getOfferById(id);
        offerValidator.checkFoundById(offer);
        offer.addTag(tag);
        offer.setTags(recreateTags(offer.getTags()));
        offer = offerDAO.update(offer);
        return offer;
    }

    @Override
    public Offer removeTag(Long id, Tag tag) {
        offerValidator.checkIdIsNotNull(id);
        tagValidator.checkProperties(tag);
        Offer offer = getOfferById(id);
        offerValidator.checkFoundById(offer);
        offer.removeTag(tag);
        offer = offerDAO.update(offer);
        return offer;
    }

    @Override
    public Offer updateCategory(Long id, Category category) {
        offerValidator.checkIdIsNotNull(id);
        categoryValidator.checkProperties(category);
        Offer offer = getOfferById(id);
        offerValidator.checkFoundById(offer);
        offer.setCategory(recreateCategory(category));
        offer = offerDAO.update(offer);
        return offer;
    }

    @Override
    public List<Offer> getAllOffers() {
        return offerDAO.findAll();
    }

    @Override
    public Offer updateOffer(Offer offer) {
        offer.fixNames();
        offerValidator.checkForUpdate(offer);
        Offer foundedOffer = offerDAO.read(offer.getId());
        offerValidator.checkFoundById(foundedOffer);
        if (!offer.getName().equals("")) {
            offerNameValidator.check(offer.getName());
            foundedOffer.setName(offer.getName());
        }
        if (offer.getPrice().getPrice() != 0L) {
            offerPriceValidator.check(offer.getPrice());
            foundedOffer.setPrice(offer.getPrice());
        }
        return foundedOffer;
    }

    @Override
    public void deleteOfferById(Long id) {
        offerValidator.checkIdIsNotNull(id);
        try {
            offerDAO.delete(id);
        } catch (EntityNotFoundException exc) {
            throw new ServiceException("Offer with id: \"" + id + "\" doesn't exist. You can't delete not existent offer.");
        }
    }

    @Override
    public List<Offer> findOffersByCategory(Category category) {
        categoryValidator.checkProperties(category);
        return offerDAO.findOffersByCategory(category);
    }

    @Override
    public List<Offer> findOffersByTags(Collection<Tag> tags) {
        tags = tags.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        if (tags.size() == 0) {
            throw new ServiceException("Tags can't be empty. Please, add, at least, one tag.");
        }
        tags.forEach(tagValidator::checkProperties);
        return offerDAO.findOffersByTags(tags);
    }

    @Override
    public List<Offer> findOffersByRangeOfPrice(double lowerBound, double upperBound) {
        priceRangeValidator.check(lowerBound, upperBound);
        return offerDAO.findOffersByRangeOfPrice(lowerBound, upperBound);
    }


    private void recreateOfferCategoryAndTags(Offer offer) {
        Category category = recreateCategory(offer.getCategory());
        offer.setCategory(category);
        Set<Tag> tags = recreateTags(offer.getTags());
        offer.setTags(tags);
    }

    private Category recreateCategory(Category category) {
        Category foundedCategory = categoryService.getCategoryByName(category.getCategory());
        categoryValidator.checkFoundByName(foundedCategory, category.getCategory());
        return foundedCategory;
    }

    private Set<Tag> recreateTags(Set<Tag> tags) {
        Set<Tag> tagSet = new HashSet<>();
        for (Tag tag : tags) {
            Tag t = tagService.getTagByName(tag.getTagname());
            tagValidator.checkFoundByName(t, tag.getTagname());
            tagSet.add(t);
        }
        return tagSet;
    }
}
