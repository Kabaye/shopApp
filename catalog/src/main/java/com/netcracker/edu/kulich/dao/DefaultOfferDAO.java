package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.List;

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
        List<Offer> offers = entityManager.createQuery("select offer from Offer offer order by offer.id", Offer.class).getResultList();
        return offers;
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
        List<Offer> offers;

        Query query = entityManager.createQuery("SELECT offer FROM Offer offer WHERE offer.category.category = :category_name");
        query.setParameter("category_name", category.getCategory());

        offers = (List<Offer>) query.getResultList();

        return offers;
    }

    @Override
    public List<Offer> findOffersByTags(Collection<Tag> tags) {
        List<Offer> offers;

        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT o FROM Offer o ");
        builder.append(" JOIN Tag t ON o MEMBER OF t.offers ");
        builder.append(" WHERE t.tagname IN (");

        long tagCount = tags.stream()
                .filter(tag -> tag != null && tag.getTagname() != null)
                .map(Tag::getTagname)
                .peek(tagname -> builder.append("'").append(tagname).append("'").append(", "))
                .count();

        builder.setLength(builder.length() - 2);
        builder.append(")");
        builder.append(" GROUP BY o.id HAVING COUNT(DISTINCT t.tagname) = ").append(tagCount);

        offers = (List<Offer>) entityManager.createQuery(builder.toString()).getResultList();

        return offers;
    }

    @Override
    public List<Offer> findOffersByRangeOfPrice(double lowerBound, double upperBound) {
        List<Offer> offers;

        Query query = entityManager.createQuery("SELECT offer FROM Offer offer " +
                "WHERE offer.price.price >= :lowerBound AND offer.price.price <= :upperBound");

        query.setParameter("lowerBound", lowerBound);
        query.setParameter("upperBound", upperBound);

        offers = (List<Offer>) query.getResultList();

        return offers;
    }


}
