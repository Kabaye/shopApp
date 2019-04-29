package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Tag;

import java.util.Set;

public interface TagDAO {
    Tag create(Tag tag);

    Tag read(Long id);

    Set<Tag> create(Set<Tag> tags);

    Tag update(Tag tag);

    void delete(Long id);
}
