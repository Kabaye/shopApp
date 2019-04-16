package com.netcracker.edu.kulich.junit;

import com.netcracker.edu.kulich.dao.OfferDAO;
import com.netcracker.edu.kulich.dao.OfferDAOManager;
import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Price;
import com.netcracker.edu.kulich.entity.Tag;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class OfferDaoTest {
    private OfferDAO offerDAO = new OfferDAOManager();

    @Test
    public void testCreateAndReadTwoOffers() {
        Offer offer = new Offer();
        offer.setName("CreateAndReadTwoOffers_offer1");

        Category category = new Category();
        category.setCategory("CreateAndReadTwoOffers_category1");

        Price price = new Price();
        price.setPrice(1950d);

        offer.setCategory(category);
        offer.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("CreateAndReadTwoOffers_tag1");
        offer.addTag(tag);

        tag = new Tag();
        tag.setTagname("CreateAndReadTwoOffers_tag2");
        offer.addTag(tag);

        offer = offerDAO.create(offer);

        category = offer.getCategory();

        offer = new Offer();
        offer.setName("CreateAndReadTwoOffers_offer2");

        price = new Price();
        price.setPrice(2500d);

        offer.setCategory(category);
        offer.setPrice(price);

        tag = new Tag();
        tag.setTagname("CreateAndReadTwoOffers_tag3");
        offer.addTag(tag);

        offer = offerDAO.create(offer);

        Offer offer2 = offerDAO.read(offer.getId());
        assertNotNull(offer);
        assertEquals(offer, offer2);
    }

    @Test
    public void testFindAllOffers() {
        List<Offer> offerList = offerDAO.findAll();

        Offer offer = new Offer();
        offer.setName("FindAllOffers_offer1");

        Category category = new Category();
        category.setCategory("FindAllOffers_category1");

        Price price = new Price();
        price.setPrice(1950d);

        offer.setCategory(category);
        offer.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("FindAllOffers_tag1");
        offer.addTag(tag);

        tag = new Tag();
        tag.setTagname("FindAllOffers_tag2");

        offer = offerDAO.create(offer);

        offerList.add(offer);

        offer = new Offer();

        offer.setName("FindAllOffers_offer2");

        category = new Category();
        category.setCategory("FindAllOffers_category2");

        price = new Price();
        price.setPrice(2500d);

        offer.setCategory(category);
        offer.setPrice(price);

        tag = new Tag();
        tag.setTagname("FindAllOffers_tag3");
        offer.addTag(tag);

        offer = offerDAO.create(offer);

        offerList.add(offer);

        List<Offer> offers = offerDAO.findAll();


        if (offers.size() != offerList.size())
            fail();
        else {
            for (int i = 0; i < offerList.size(); i++) {
                assertEquals(offerList.get(i), offers.get(i));
            }
        }
    }

    @Test
    public void testUpdateOffer() {
        Offer offer = new Offer();
        offer.setName("UpdateOffer_offer1");

        Category category = new Category();
        category.setCategory("UpdateOffer_category1");

        Price price = new Price();
        price.setPrice(1950d);

        offer.setCategory(category);
        offer.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("UpdateOffer_tag1");
        offer.addTag(tag);

        tag = new Tag();
        tag.setTagname("UpdateOffer_tag2");

        offer = offerDAO.create(offer);

        offer.setName("new UpdateOffer_offer1");
        offer.getPrice().setPrice(666d);
        offer.getCategory().setCategory("new UpdateOffer_category1");
        offer.getTags().clear();

        offerDAO.update(offer);

        Offer offer1 = offerDAO.read(offer.getId());
        assertNotNull(offer1);
        assertEquals(offer, offer1);
    }

    @Test
    public void testDeleteOffer() {
        List<Offer> offerList = offerDAO.findAll();

        Offer offer = new Offer();
        offer.setName("DeleteOffer_offer1");

        Category category = new Category();
        category.setCategory("DeleteOffer_category1");

        Price price = new Price();
        price.setPrice(2500d);

        offer.setCategory(category);
        offer.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("DeleteOffer_tag1");
        offer.addTag(tag);

        tag = new Tag();
        tag.setTagname("DeleteOffer_tag2");

        offer = offerDAO.create(offer);

        offerDAO.delete(offer.getId());

        List<Offer> offers = offerDAO.findAll();

        if (offers.size() != offerList.size())
            fail();
        else {
            for (int i = 0; i < offerList.size(); i++) {
                assertEquals(offerList.get(i), offers.get(i));
            }
        }
    }
}
