package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.dao.OfferDAO;
import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Tag;
import com.netcracker.edu.kulich.exception.service.OfferServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service(value = "offerService")
public class DefaultOfferService implements OfferService {
    private static final String NOT_CORRECT_ARGUMENT_EXCEPTION_MESSAGE = "Offer's arguments are invalid, please, set them valid.";
    private static final String DELETING_OR_UPDATING_NOT_EXISTENT_OFFER = "You try to delete / update not existent offer.";
    private static final String ADDING_TO_OFFER_ALREADY_ADDED_TAG = "You try to add tag to offer, which already contains this tag.";
    private static final String UPDATING_NOT_EXISTENT_OFFER_WITH_TAG_OR_CATEGORY = "You try to add / delete tag or update category of not existent offer.";

    private static final double MIN_PRICE = 1.0;
    @Autowired
    private OfferDAO offerDAO;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Override
    public Offer saveOffer(Offer offer) {
        offer.offerNameFixing();
        offerNameAndPriceChecking(offer);
        offerCategoryAndTagCheckingAndRecreating(offer);
        offer = offerDAO.create(offer);
        return offer;
    }

    @Override
    public Offer getOfferById(Long id) {
        return offerDAO.read(id);
    }

    @Override
    public Offer addTag(Long id, Tag tag) {
        Offer offer = getOfferById(id);
        if (offer == null) {
            throw new OfferServiceException(UPDATING_NOT_EXISTENT_OFFER_WITH_TAG_OR_CATEGORY);
        }
        if (!offer.addTag(tag)) {
            throw new OfferServiceException(ADDING_TO_OFFER_ALREADY_ADDED_TAG);
        }
        offer.setTags(tagsRecreating(offer.getTags()));
        offer = offerDAO.update(offer);
        return offer;
    }

    @Override
    public Offer removeTag(Long id, Tag tag) {
        Offer offer = getOfferById(id);
        if (offer == null) {
            throw new OfferServiceException(UPDATING_NOT_EXISTENT_OFFER_WITH_TAG_OR_CATEGORY);
        }
        if (offer.removeTag(tag)) {
            offer = offerDAO.update(offer);
        } else {
            throw new OfferServiceException(NOT_CORRECT_ARGUMENT_EXCEPTION_MESSAGE);
        }
        return offer;
    }

    @Override
    public Offer updateCategory(Long id, Category category) {
        Offer offer = getOfferById(id);
        if (offer == null) {
            throw new OfferServiceException(UPDATING_NOT_EXISTENT_OFFER_WITH_TAG_OR_CATEGORY);
        }
        if (offer.getCategory().getCategory().equals(category.getCategory())) {
            throw new OfferServiceException(NOT_CORRECT_ARGUMENT_EXCEPTION_MESSAGE);
        }
        offer.setCategory(categoryRecreating(category));
        offer = offerDAO.update(offer);
        return offer;
    }

    @Override
    public List<Offer> getAllOffers() {
        return offerDAO.findAll();
    }

    @Override
    public Offer updateOffer(Offer offer) {
        Offer offer1 = offerDAO.read(offer.getId());
        if (offer1 == null) {
            throw new OfferServiceException(DELETING_OR_UPDATING_NOT_EXISTENT_OFFER);
        }
        offer.offerNameFixing();
        if (offer.getName().equals("") && offer.getPrice().getPrice() == 0.0) {
            throw new OfferServiceException(NOT_CORRECT_ARGUMENT_EXCEPTION_MESSAGE);
        }
        if (offer.getName().equals("")) {
            offer.setName(offer1.getName());
        }
        if (offer.getPrice().getPrice() == 0.0) {
            offer.setPrice(offer1.getPrice());
        }

        offer.setTags(offer1.getTags());
        offer.setCategory(offer1.getCategory());

        offerNameAndPriceChecking(offer);
        offer = offerDAO.update(offer);
        return offer;
    }

    @Override
    public void deleteOfferById(Long id) {
        try {
            offerDAO.delete(id);
        } catch (EntityNotFoundException exc) {
            throw new OfferServiceException(DELETING_OR_UPDATING_NOT_EXISTENT_OFFER);
        }
    }

    @Override
    public List<Offer> findOffersByCategory(Category category) {
        return offerDAO.findOffersByCategory(category);
    }

    @Override
    public List<Offer> findOffersByTags(Collection<Tag> tags) {
        tags = tags.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        return offerDAO.findOffersByTags(tags);
    }

    @Override
    public List<Offer> findOffersByRangeOfPrice(double lowerBound, double upperBound) {
        if (lowerBound < MIN_PRICE || lowerBound > upperBound || upperBound < MIN_PRICE)
            throw new OfferServiceException(NOT_CORRECT_ARGUMENT_EXCEPTION_MESSAGE);
        return offerDAO.findOffersByRangeOfPrice(lowerBound, upperBound);
    }

    private void offerNameAndPriceChecking(Offer offer) {
        if (offer.getName().length() <= 3) {
            throw new OfferServiceException(NOT_CORRECT_ARGUMENT_EXCEPTION_MESSAGE);
        }
        if (offer.getPrice().getPrice() < MIN_PRICE) {
            throw new OfferServiceException(NOT_CORRECT_ARGUMENT_EXCEPTION_MESSAGE);
        }
    }

    private void offerCategoryAndTagCheckingAndRecreating(Offer offer) {
        Category category = categoryRecreating(offer.getCategory());
        offer.setCategory(category);
        Set<Tag> tags = tagsRecreating(offer.getTags());
        offer.setTags(tags);
    }

    private Category categoryRecreating(Category category) {
        if (category == null) {
            throw new OfferServiceException(NOT_CORRECT_ARGUMENT_EXCEPTION_MESSAGE);
        }
        category.categoryNameFixing();
        if (category.getCategory().length() < 2) {
            throw new OfferServiceException(NOT_CORRECT_ARGUMENT_EXCEPTION_MESSAGE);
        }
        Category category1 = categoryService.getCategoryByName(category.getCategory());
        return category1 == null ? category : category1;
    }

    private Set<Tag> tagsRecreating(Set<Tag> tags) {
        tags = tags.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Set<Tag> tagSet = new HashSet<>();
        for (Tag tag : tags) {
            tag.tagNameFixing();
            if (tag.getTagname().length() < 3) {
                throw new OfferServiceException(NOT_CORRECT_ARGUMENT_EXCEPTION_MESSAGE);
            }
            Tag t = tagService.getTagByName(tag.getTagname());
            tagSet.add(t == null ? tag : t);
        }
        return tagSet;
    }
}
