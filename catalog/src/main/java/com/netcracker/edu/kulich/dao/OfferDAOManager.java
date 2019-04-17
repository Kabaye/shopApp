package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Tag;
import com.netcracker.edu.kulich.utils.PostgreSQLEntityManagerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.Collection;
import java.util.List;

public class OfferDAOManager implements OfferDAO {

    private EntityManagerFactory entityManagerFactory = PostgreSQLEntityManagerFactory.getDatabaseManagerFactory();
    private EntityManager entityManager;

    @Override
    public Offer create(Offer offer) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction;
        transaction = entityManager.getTransaction();
        transaction.begin();
        offer = entityManager.merge(offer);
        transaction.commit();
        entityManager.close();
        return offer;
    }

    @Override
    public Offer read(Long id) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Offer foundOffer = entityManager.find(Offer.class, id);
        entityManager.refresh(foundOffer);
        transaction.commit();
        entityManager.close();
        return foundOffer;
    }

    @Override
    public List<Offer> findAll() {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<Offer> offers;
        transaction.begin();
        offers = entityManager.createQuery("select offer from Offer offer order by offer.id", Offer.class).getResultList();
        transaction.commit();
        entityManager.close();
        return offers;
    }


    @Override
    public Offer update(Offer offer) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        offer = entityManager.merge(offer);
        transaction.commit();
        entityManager.close();
        return offer;
    }

    @Override
    public void delete(Long id) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction;
        transaction = entityManager.getTransaction();
        transaction.begin();
        Offer offer = entityManager.getReference(Offer.class, id);
        entityManager.remove(offer);
        transaction.commit();
        entityManager.close();
    }

    @Override
    public List<Offer> findOffersByCategory(Category category) {
        List<Offer> offers;

        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Query query = entityManager.createQuery("SELECT offer FROM Offer offer WHERE offer.category.category = :category_name");

        query.setParameter("category_name", category.getCategory());

        offers = (List<Offer>) query.getResultList();

        entityManager.getTransaction().commit();
        entityManager.close();

        return offers;
    }

    @Override
    public List<Offer> findOffersByTags(Collection<Tag> tags) {
        List<Offer> offers;

        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

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

        entityManager.getTransaction().commit();
        entityManager.close();

        return offers;
    }

    @Override
    public List<Offer> findOffersByRangeOfPrice(double lowerBound, double upperBound) {
        List<Offer> offers;

        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Query query = entityManager.createQuery("SELECT offer FROM Offer offer " +
                "WHERE offer.price.price >= :lowerBound AND offer.price.price <= :upperBound");

        query.setParameter("lowerBound", lowerBound);
        query.setParameter("upperBound", upperBound);

        offers = (List<Offer>) query.getResultList();

        entityManager.getTransaction().commit();
        entityManager.close();

        return offers;
    }


}
