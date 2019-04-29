package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Tag;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Set;

@Repository(value = "tagDAO")
@Transactional
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
        Tag tag = entityManager.find(Tag.class, id);
        return tag;
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
    public void delete(Long id) {
        Tag tag = entityManager.getReference(Tag.class, id);
        for (Offer offer : tag.getOffers()) {
            offer.getTags().remove(tag);
        }
        entityManager.remove(tag);
    }
}
