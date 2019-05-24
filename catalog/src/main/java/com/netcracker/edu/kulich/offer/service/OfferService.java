package com.netcracker.edu.kulich.offer.service;

import com.netcracker.edu.kulich.category.entity.Category;
import com.netcracker.edu.kulich.offer.entity.Offer;
import com.netcracker.edu.kulich.tag.entity.Tag;

import java.util.Collection;
import java.util.List;

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
