package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.dao.OfferDAO;
import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Tag;
import com.netcracker.edu.kulich.service.exception.OfferServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service(value = "offerService")
public class DefaultOfferService implements OfferService {
    private static final String NULL_OFFER_CATEGORY_EXCEPTION_MESSAGE = "Category is empty, please, set it not empty.";
    private static final String NULL_OFFER_CATEGORY_NAME_EXCEPTION_MESSAGE = "Category name is empty, please, set it not empty.";
    private static final String NULL_OFFER_TAG_NAME_EXCEPTION_MESSAGE = "One of offer's tag has empty name, please, set it not empty.";
    private static final String NOT_CORRECT_PRICE_BOUNDS_EXCEPTION_MESSAGE = "You set not correct values of price, please, set they correctly.";
    private static final String DELETING_NOT_EXISTENT_OFFER = "You try to delete not existent category.";

    private static final double MIN_PRICE = 1.0;

    @Autowired
    private OfferDAO offerDAO;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Override
    public Offer saveOffer(Offer offer) throws OfferServiceException {
        offerCheckingAndRecreating(offer);
        offer = offerDAO.create(offer);
        return offer;
    }

    @Override
    public Offer getOfferById(Long id) {
        return offerDAO.read(id);
    }

    @Override
    public List<Offer> getAllOffers() {
        return offerDAO.findAll();
    }

    @Override
    public Offer updateOffer(Offer offer) throws OfferServiceException {
        offerCheckingAndRecreating(offer);
        offer = offerDAO.update(offer);
        return offer;
    }

    @Override
    public void deleteOfferById(Long id) throws OfferServiceException {
        try {
            offerDAO.delete(id);
        } catch (EntityNotFoundException exc) {
            throw new OfferServiceException(DELETING_NOT_EXISTENT_OFFER);
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
    public List<Offer> findOffersByRangeOfPrice(double lowerBound, double upperBound) throws OfferServiceException {
        if (lowerBound < MIN_PRICE || lowerBound > upperBound || upperBound < MIN_PRICE)
            throw new OfferServiceException(NOT_CORRECT_PRICE_BOUNDS_EXCEPTION_MESSAGE);
        return offerDAO.findOffersByRangeOfPrice(lowerBound, upperBound);
    }

    private void offerCheckingAndRecreating(Offer offer) throws OfferServiceException {
        Category category = categoryRecreating(offer.getCategory());
        offer.setCategory(category);
        Set<Tag> tags = tagsRecreating(offer.getTags());
        offer.setTags(tags);
    }

    private Category categoryRecreating(Category category) throws OfferServiceException {
        if (category == null) {
            throw new OfferServiceException(NULL_OFFER_CATEGORY_EXCEPTION_MESSAGE);
        }
        if (category.getCategory().equals("")) {
            throw new OfferServiceException(NULL_OFFER_CATEGORY_NAME_EXCEPTION_MESSAGE);
        }
        Category category1 = categoryService.getCategoryByName(category.getCategory());
        return category1 == null ? category : category1;
    }

    private Set<Tag> tagsRecreating(Set<Tag> tags) throws OfferServiceException {
        tags = tags.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Set<Tag> tagSet = new HashSet<>();
        for (Tag tag : tags) {
            if (tag.getTagname().equals("")) {
                throw new OfferServiceException(NULL_OFFER_TAG_NAME_EXCEPTION_MESSAGE);
            }
            Tag t = tagService.getTagByName(tag.getTagname());
            tagSet.add(t == null ? tag : t);
        }
        return tagSet;
    }
}
