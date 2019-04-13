package com.netcracker.edu.kulich.junit;

import com.netcracker.edu.kulich.dao.*;
import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Price;
import com.netcracker.edu.kulich.entity.Tag;
import com.netcracker.edu.kulich.utils.PostgreSQLDatabaseManager;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OfferDaoTest {
    private OfferDAO offerDAO = new OfferDAOManager();
    private CategoryDAO categoryDAO = new CategoryDAOManager();
    private TagDAO tagDAO = new TagDAOManager();
    private EntityManager manager = PostgreSQLDatabaseManager.getInstance().getEntityManager();

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
        offer.addTag(tag);

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
        assertEquals(offer.toString(), offer2.toString());
    }

    @Test
    public void testFindAllOffers() {
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
        assertEquals(offerList.toString(), offers.toString());
    }

    @Test
    public void testUpdateOffer() {
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
        assertEquals(offer.toString(), offer1.toString());
    }

    @Test
    public void testDeleteOffer() {
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
        assertEquals(offerList.toString(), offers.toString());
    }
}
