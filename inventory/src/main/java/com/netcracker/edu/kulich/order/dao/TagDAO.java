package com.netcracker.edu.kulich.order.dao;

import com.netcracker.edu.kulich.order.entity.Tag;

public interface TagDAO {
    Tag create(Tag tag);

    Tag readByName(String name);
}
