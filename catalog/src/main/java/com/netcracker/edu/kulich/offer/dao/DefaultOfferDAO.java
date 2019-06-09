package com.netcracker.edu.kulich.offer.dao;

import com.netcracker.edu.kulich.category.entity.Category;
import com.netcracker.edu.kulich.offer.entity.Offer;
import com.netcracker.edu.kulich.tag.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository(value = "offerDAO")
public class DefaultOfferDAO implements OfferDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Offer create(Offer offer) {
        offer = entityManager.merge(offer);
        return offer;
    }

    @Override
    public Offer read(Long id) {
        Offer foundOffer = entityManager.find(Offer.class, id);
        if (foundOffer != null) {
            entityManager.refresh(foundOffer);
        }
        return foundOffer;
    }

    @Override
    public List<Offer> findAll() {
        return (List<Offer>) entityManager.createNamedQuery("Offer.findAll").getResultList();
    }


    @Override
    public Offer update(Offer offer) {
        offer = entityManager.merge(offer);
        return offer;
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {
        Offer offer = entityManager.find(Offer.class, id);
        if (offer == null)
            throw new EntityNotFoundException();
        entityManager.remove(offer);
    }

    @Override
    public List<Offer> findOffersByCategory(Category category) {
        return (List<Offer>) entityManager.createNamedQuery("Offer.findAllByCategory").setParameter("category_name", category.getCategory()).getResultList();
    }

    @Override
    public List<Offer> findOffersByTags(Collection<Tag> tags) {
        return entityManager.createNamedQuery("Offer.findAllHavingTags", Offer.class)
                .setParameter("tagNameList", tags.stream()
                        .map(Tag::getTagname)
                        .collect(Collectors.toList()))
                .setParameter("tagCount", (long) tags.size())
                .getResultList();
    }

    @Override
    public List<Offer> findOffersByRangeOfPrice(double lowerBound, double upperBound) {
        return entityManager.createNamedQuery("Offer.findAllWithPriceInRange", Offer.class)
                .setParameter("lowerBound", lowerBound)
                .setParameter("upperBound", upperBound)
                .getResultList();
    }


}
