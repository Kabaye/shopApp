package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Offer;

import java.util.List;

public interface OfferDAO {
    Offer create(Offer offer);
    Offer read(Long id);
    List<Offer> findAll();
    Offer update(Offer offer);
    void delete(Long id);
}
