package com.netcracker.edu.kulich.offer.dao;

import com.netcracker.edu.kulich.category.entity.Category;
import com.netcracker.edu.kulich.offer.entity.Offer;
import com.netcracker.edu.kulich.tag.entity.Tag;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;

public interface OfferDAO {
    Offer create(Offer offer);

    Offer read(Long id);

    List findAll();

    Offer update(Offer offer);

    void delete(Long id) throws EntityNotFoundException;

    List<Offer> findOffersByCategory(Category category);

    List<Offer> findOffersByTags(Collection<Tag> tags);

    List<Offer> findOffersByRangeOfPrice(double lowerBound, double upperBound);
}
