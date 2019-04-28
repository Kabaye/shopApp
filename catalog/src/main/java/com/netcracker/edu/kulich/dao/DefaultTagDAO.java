package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;
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
        Tag tag = entityManager.find(Tag.class, id);
        return tag;
    }

    @Override
    public Tag readByName(String name) {
        List objects = entityManager.createQuery("SELECT tag FROM Tag tag WHERE tag.tagname = :name").setParameter("name", name).getResultList();
        Tag tag = null;
        if (objects.size() != 0)
            tag = (Tag) objects.get(0);
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
