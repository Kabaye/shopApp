package com.netcracker.edu.kulich.junit;

import com.netcracker.edu.kulich.dao.OfferDAO;
import com.netcracker.edu.kulich.dao.OfferDAOImplementation;
import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Price;
import com.netcracker.edu.kulich.entity.Tag;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OfferDaoTest {
    private OfferDAO offerDAO = new OfferDAOImplementation();

    @Test
    public void testCreateAndReadOneOffer() {
        Offer offer = new Offer();
        offer.setName("of1");

        Category category = new Category();
        category.setCategory("cat1");

        Price price = new Price();
        price.setPrice(1950d);

        offer.setCategory(category);
        offer.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("tag1");
        offer.addTag(tag);

        tag = new Tag();
        tag.setTagname("tag2");

        OfferDAO offerDAO = new OfferDAOImplementation();
        offerDAO.create(offer);

        Offer offer1 = offerDAO.read(offer.getId());
        assertNotNull(offer1);
        assertEquals(offer1.toString(), offer.toString());
    }

    @Test
    public void testCreateAndReadTwoOffers() {
        Offer offer = new Offer();
        offer.setName("of1");

        Category category = new Category();
        category.setCategory("cat1");

        Price price = new Price();
        price.setPrice(1950d);

        offer.setCategory(category);
        offer.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("tag1");
        offer.addTag(tag);

        tag = new Tag();
        tag.setTagname("tag2");

        OfferDAO offerDAO = new OfferDAOImplementation();
        offerDAO.create(offer);

        offer = new Offer();
        offer.setName("of2");
        category = new Category();
        category.setCategory("cat1");

        price = new Price();
        price.setPrice(2500d);

        offer.setCategory(category);
        offer.setPrice(price);

        tag = new Tag();
        tag.setTagname("tag3");
        offer.addTag(tag);

        offerDAO.create(offer);

        Offer offer2 = offerDAO.read(offer.getId());
        assertNotNull(offer);
        assertEquals(offer2.toString(), offer.toString());
    }

    @Test
    public void testFindAllOffers() {

        OfferDAO offerDAO = new OfferDAOImplementation();
        List<Offer> offerList = offerDAO.findAll();

        Offer offer = new Offer();
        offer.setName("of1");

        Category category = new Category();
        category.setCategory("cat1");

        Price price = new Price();
        price.setPrice(1950d);

        offer.setCategory(category);
        offer.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("tag1");
        offer.addTag(tag);

        tag = new Tag();
        tag.setTagname("tag2");


        offerDAO.create(offer);

        offerList.add(offer);

        offer = new Offer();

        offer.setName("of2");

        category = new Category();
        category.setCategory("cat2");

        price = new Price();
        price.setPrice(2500d);

        offer.setCategory(category);
        offer.setPrice(price);

        tag = new Tag();
        tag.setTagname("tag3");
        offer.addTag(tag);

        offerDAO.create(offer);
        offerList.add(offer);

        List<Offer> offers = offerDAO.findAll();
        assertNotNull(offers);
        assertEquals(offers.toString(), offerList.toString());
    }

    @Test
    public void testUpdateOffer() {
        OfferDAO offerDAO = new OfferDAOImplementation();

        Offer offer = new Offer();
        offer.setName("of1");

        Category category = new Category();
        category.setCategory("cat1");

        Price price = new Price();
        price.setPrice(1950d);

        offer.setCategory(category);
        offer.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("tag1");
        offer.addTag(tag);

        tag = new Tag();
        tag.setTagname("tag2");

        offerDAO.create(offer);

        offer.setName("new of1");
        offer.getPrice().setPrice(666d);
        offer.getCategory().setCategory("new cat1");
        offer.getTags().clear();

        offerDAO.update(offer);

        Offer offer1 = offerDAO.read(offer.getId());
        assertNotNull(offer1);
        assertEquals(offer1.toString(),offer.toString());
    }

    @Test
    public void testDeleteOffer() {
        OfferDAO offerDAO = new OfferDAOImplementation();
        List<Offer> offerList = offerDAO.findAll();

        Offer offer = new Offer();
        offer.setName("of1");

        Category category = new Category();
        category.setCategory("cat1");

        Price price = new Price();
        price.setPrice(2500d);

        offer.setCategory(category);
        offer.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("tag1");
        offer.addTag(tag);

        tag = new Tag();
        tag.setTagname("tag2");

        offerDAO.create(offer);

        offerDAO.delete(offer.getId());

        List<Offer> offers = offerDAO.findAll();

        assertNotNull(offers);
        assertEquals(offers.toString(),offerList.toString());
    }
}
