package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.dao.OfferDAO;
import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Tag;
import com.netcracker.edu.kulich.exception.service.ServiceException;
import com.netcracker.edu.kulich.logging.DefaultLogging;
import com.netcracker.edu.kulich.logging.Logging;
import com.netcracker.edu.kulich.service.validation.NameValidator;
import com.netcracker.edu.kulich.service.validation.PriceRangeValidator;
import com.netcracker.edu.kulich.service.validation.PriceValidator;
import com.netcracker.edu.kulich.service.validation.ServiceValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service(value = "offerService")
@DefaultLogging
public class DefaultOfferService implements OfferService {
    private static final Logger logger = LoggerFactory.getLogger(OfferService.class);

    private OfferDAO offerDAO;
    private CategoryService categoryService;
    private TagService tagService;
    private ServiceValidator<Offer, Long> offerValidator;
    private ServiceValidator<Tag, Long> tagValidator;
    private ServiceValidator<Category, Long> categoryValidator;
    private NameValidator offerNameValidator;
    private PriceValidator offerPriceValidator;
    private PriceRangeValidator priceRangeValidator;

    public DefaultOfferService(OfferDAO offerDAO, CategoryService categoryService, TagService tagService, ServiceValidator<Offer,
            Long> offerValidator, ServiceValidator<Tag, Long> tagValidator, ServiceValidator<Category, Long> categoryValidator,
                               NameValidator offerNameValidator, PriceValidator offerPriceValidator, PriceRangeValidator priceRangeValidator) {
        this.offerDAO = offerDAO;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.offerValidator = offerValidator;
        this.tagValidator = tagValidator;
        this.categoryValidator = categoryValidator;
        this.offerNameValidator = offerNameValidator;
        this.offerPriceValidator = offerPriceValidator;
        this.priceRangeValidator = priceRangeValidator;
    }

    @Override
    @Logging(startMessage = "Request on saving offer to database is received.", endMessage = "Offer is successfully saved to database.")
    public Offer saveOffer(Offer offer) {
        offer.fixNames();
        offerValidator.checkForPersist(offer);
        recreateOfferCategoryAndTags(offer);
        offer = offerDAO.create(offer);
        return offer;
    }

    @Override
    @Logging(startMessage = "Request on getting offer by id from database is received.", endMessage = "Offer is successfully get from database.")
    public Offer getOfferById(Long id) {
        offerValidator.checkIdIsNotNull(id);
        return offerDAO.read(id);
    }

    @Override
    @Logging(startMessage = "Request on updating offer (adding tag) in database is received.", endMessage = "Offer is successfully updated in database.")
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
    @Logging(startMessage = "Request on updating offer (removing tag) in database is received.", endMessage = "Offer is successfully updated in database.")
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
    @Logging(startMessage = "Request on updating offer (changing category) in database is received.", endMessage = "Offer is successfully updated in database.")
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
    @Logging(startMessage = "Request on getting all offers from database is received.", endMessage = "Offers are successfully get from database.")
    public List<Offer> getAllOffers() {
        return offerDAO.findAll();
    }

    @Override
    @Logging(startMessage = "Request on updating offer in database is received.", endMessage = "Offer is successfully updated in database.")
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
    @Logging(startMessage = "Request on deleting offer from database is received.", endMessage = "Offer is successfully deleted from database.")
    public void deleteOfferById(Long id) {
        offerValidator.checkIdIsNotNull(id);
        try {
            offerDAO.delete(id);
        } catch (EntityNotFoundException exc) {
            logger.error("Attempt to delete not existent offer.");
            throw new ServiceException("Offer with id: \"" + id + "\" doesn't exist. You can't delete not existent offer.");
        }
    }

    @Override
    @Logging(startMessage = "Request on getting all offers by category from database is received.", endMessage = "Offers are successfully get from database.")
    public List<Offer> findOffersByCategory(Category category) {
        categoryValidator.checkProperties(category);
        return offerDAO.findOffersByCategory(category);
    }

    @Override
    @Logging(startMessage = "Request on getting all offers by tags from database is received.", endMessage = "Offers are successfully get from database.")
    public List<Offer> findOffersByTags(Collection<Tag> tags) {
        tags = tags.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        if (tags.size() == 0) {
            logger.error("Attempt to send empty tags.");
            throw new ServiceException("Tags can't be empty. Please, add, at least, one tag.");
        }
        tags.forEach(tagValidator::checkProperties);
        return offerDAO.findOffersByTags(tags);
    }

    @Override
    @Logging(startMessage = "Request on getting all offers by range of price from database is received.", endMessage = "Offers are successfully get from database.")
    public List<Offer> findOffersByRangeOfPrice(double lowerBound, double upperBound) {
        priceRangeValidator.check(lowerBound, upperBound);
        List<Offer> offers = offerDAO.findOffersByRangeOfPrice(lowerBound, upperBound);
        offers.sort(Comparator.comparingDouble(of -> of.getPrice().getPrice()));
        return offers;
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
