package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Tag;
import com.netcracker.edu.kulich.service.exception.OfferServiceException;

import java.util.Collection;
import java.util.List;

public interface OfferService {
    Offer saveOffer(Offer offer) throws OfferServiceException;

    Offer getOfferById(Long id);

    List<Offer> getAllOffers();

    Offer updateOffer(Offer offer) throws OfferServiceException;

    void deleteOfferById(Long id) throws OfferServiceException;

    List<Offer> findOffersByCategory(Category category);

    List<Offer> findOffersByTags(Collection<Tag> tags);

    List<Offer> findOffersByRangeOfPrice(double lowerBound, double upperBound) throws OfferServiceException;
}
