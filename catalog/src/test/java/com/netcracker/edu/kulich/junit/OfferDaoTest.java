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

        offer = offerDAO.create(offer);

        offer = new Offer();
        offer.setName("of2");

        price = new Price();
        price.setPrice(2500d);

        offer.setCategory(category);
        offer.setPrice(price);

        tag = new Tag();
        tag.setTagname("tag3");
        offer.addTag(tag);

        offer = offerDAO.create(offer);

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

        offer = offerDAO.create(offer);

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

        offer = offerDAO.create(offer);

        offerList.add(offer);

        List<Offer> offers = offerDAO.findAll();

        StringBuilder builder = new StringBuilder();
        StringBuilder expectedBuilder = new StringBuilder();
        if (offers.size() != offerList.size())
            fail();
        else {
            for (int i = 0; i < offerList.size(); i++) {
                builder.append(offers.get(i).getId());
                builder.append(offers.get(i).getCategory().getId());
                expectedBuilder.append(offerList.get(i).getId());
                expectedBuilder.append(offerList.get(i).getCategory().getId());
                builder.append(offers.get(i).getTags().size());
                expectedBuilder.append(offerList.get(i).getTags().size());
            }
            assertEquals(expectedBuilder.toString(), builder.toString());
        }
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

        offer = offerDAO.create(offer);

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

        offer = offerDAO.create(offer);

        offerDAO.delete(offer.getId());

        List<Offer> offers = offerDAO.findAll();

        assertNotNull(offers);
        assertEquals(offerList.toString(), offers.toString());
    }
}
