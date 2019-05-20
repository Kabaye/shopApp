package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Tag;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Transactional
public interface OfferService {
    Offer saveOffer(Offer offer);

    Offer getOfferById(Long id);

    List<Offer> getAllOffers();

    Offer updateOffer(Offer offer);

    Offer addTag(Long id, Tag tag);

    Offer removeTag(Long id, Tag tag);

    Offer updateCategory(Long id, Category category);

    void deleteOfferById(Long id);

    List<Offer> findOffersByCategory(Category category);

    List<Offer> findOffersByTags(Collection<Tag> tags);

    List<Offer> findOffersByRangeOfPrice(double lowerBound, double upperBound);
}
