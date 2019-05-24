package com.netcracker.edu.kulich.tag.dao;

import com.netcracker.edu.kulich.tag.entity.Tag;

import javax.persistence.EntityNotFoundException;
import java.util.Set;

public interface TagDAO {
    Tag create(Tag tag);

    Tag read(Long id);

    Tag readByName(String name);

    Set<Tag> create(Set<Tag> tags);

    Tag update(Tag tag);

    void delete(Long id) throws EntityNotFoundException;
}
