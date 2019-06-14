package com.netcracker.edu.kulich.tag.dao;

import com.netcracker.edu.kulich.offer.entity.Offer;
import com.netcracker.edu.kulich.tag.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.Set;

@Repository(value = "tagDAO")
public class DefaultTagDAO implements TagDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Tag create(Tag tag) {
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public Tag read(Long id) {
        return entityManager.find(Tag.class, id);
    }

    @Override
    public Tag readByName(String name) {
        return entityManager.createNamedQuery("Tag.findByName", Tag.class)
                .setParameter("tagname", name)
                .setMaxResults(1).getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Set<Tag> create(Set<Tag> tags) {
        for (Tag elem : tags) {
            entityManager.persist(elem);
        }
        return tags;
    }

    @Override
    public Tag update(Tag tag) {
        tag = entityManager.merge(tag);
        return tag;
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {
        Tag tag = entityManager.find(Tag.class, id);
        if (tag == null)
            throw new EntityNotFoundException();
        for (Offer offer : tag.getOffers()) {
            offer.getTags().remove(tag);
        }
        entityManager.remove(tag);
    }
}
