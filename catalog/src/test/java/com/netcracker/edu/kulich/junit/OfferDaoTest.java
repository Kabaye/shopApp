package com.netcracker.edu.kulich.junit;

import com.netcracker.edu.kulich.dao.OfferDAO;
import com.netcracker.edu.kulich.dao.TagDAO;
import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Price;
import com.netcracker.edu.kulich.entity.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OfferDaoTest {
    @Autowired
    private OfferDAO offerDAO;
    @Autowired
    private TagDAO tagDAO;

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
        offer.addTag(tag);

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
        offer.addTag(tag);

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
        offer.addTag(tag);

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

    @Test
    public void testFindAllOffersWithCategory() {
        List<Offer> offerList = new ArrayList<>();

        Offer offer = new Offer();
        offer.setName("FindAllOffersWithCategory_offer1");

        Category category = new Category();
        category.setCategory("FindAllOffersWithCategory_category1");

        Price price = new Price();
        price.setPrice(2500d);

        offer.setCategory(category);
        offer.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("FindAllOffersWithCategory_tag1");
        offer.addTag(tag);

        Tag tag1 = new Tag();
        tag1.setTagname("FindAllOffersWithCategory_tag2");
        offer.addTag(tag1);

        offer = offerDAO.create(offer);

        offerList.add(offer);

        category = offer.getCategory();

        tag = (Tag) offer.getTags().toArray()[0];
        tag1 = (Tag) offer.getTags().toArray()[1];

        offer = new Offer();

        offer.setCategory(category);

        price = new Price();
        price.setPrice(2000d);

        offer.setPrice(price);

        offer.setName("FindAllOffersWithCategory_offer2");

        offer.addTag(tag);
        offer.addTag(tag1);

        offer = offerDAO.create(offer);

        offerList.add(offer);

        offer = new Offer();

        Category category1 = new Category();
        category1.setCategory("FindAllOffersWithCategory_category2");

        offer.setCategory(category1);

        price = new Price();
        price.setPrice(1500d);

        offer.setPrice(price);

        offer.setName("FindAllOffersWithCategory_offer3");

        offer.addTag(tag);
        offer.addTag(tag1);

        offerDAO.create(offer);

        List<Offer> offers = offerDAO.findOffersByCategory(category);

        assertEquals(offerList.size(), offers.size());
        for (int i = 0; i < offerList.size(); i++) {
            assertEquals(offerList.get(i), offers.get(i));
        }

    }

    @Test
    public void testFindAllOffersWithTags() {
        Tag[] tags = new Tag[5];

        for (int i = 0; i < 5; i++) {
            tags[i] = new Tag();
            tags[i].setTagname("FindAllOffersWithTags_tag" + (i + 1));
            tags[i] = tagDAO.create(tags[i]);
        }

        Category category = new Category();
        category.setCategory("FindAllOffersWithTags_category1");

        Price price = new Price();
        price.setPrice(666D);

        Offer[] offers = new Offer[4];

        // 0,1,4

        offers[0] = new Offer();
        offers[0].setName("FindAllOffersWithTags_offer1");
        offers[0].setPrice(price);
        offers[0].setCategory(category);
        offers[0].addTag(tags[0]);
        offers[0].addTag(tags[1]);
        offers[0].addTag(tags[4]);

        offers[0] = offerDAO.create(offers[0]);
        category = offers[0].getCategory();

        //1,3,4

        offers[1] = new Offer();
        offers[1].setName("FindAllOffersWithTags_offer2");
        offers[1].setPrice(price);
        offers[1].setCategory(category);
        offers[1].addTag(tags[1]);
        offers[1].addTag(tags[3]);
        offers[1].addTag(tags[4]);

        offerDAO.create(offers[1]);

        //2,3,4

        offers[2] = new Offer();
        offers[2].setName("FindAllOffersWithTags_offer3");
        offers[2].setPrice(price);
        offers[2].setCategory(category);
        offers[2].addTag(tags[2]);
        offers[2].addTag(tags[3]);
        offers[2].addTag(tags[4]);
        offerDAO.create(offers[2]);

        //0,4

        offers[3] = new Offer();
        offers[3].setName("FindAllOffersWithTags_offer4");
        offers[3].setPrice(price);
        offers[3].setCategory(category);
        offers[3].addTag(tags[0]);
        offers[3].addTag(tags[4]);
        offerDAO.create(offers[3]);

        assertEquals(2, offerDAO.findOffersByTags(Arrays.asList(tags[0])).size());
        assertEquals(4, offerDAO.findOffersByTags(Arrays.asList(tags[4])).size());
        assertEquals(2, offerDAO.findOffersByTags(Arrays.asList(tags[0], tags[4])).size());
        assertEquals(1, offerDAO.findOffersByTags(Arrays.asList(tags[2], tags[3], tags[4])).size());
        assertEquals(1, offerDAO.findOffersByTags(Arrays.asList(tags[2], tags[3], tags[4])).size());
        assertEquals(2, offerDAO.findOffersByTags(Arrays.asList(tags[1], tags[4])).size());
    }

    @Test
    public void testFindAllOffersInRangeOfPrice() {
        int i1, i2, i3, i4, i5;
        i1 = offerDAO.findOffersByRangeOfPrice(1500d, 1900d).size();
        i2 = offerDAO.findOffersByRangeOfPrice(1000d, 1000d).size();
        i3 = offerDAO.findOffersByRangeOfPrice(900d, 1100d).size();
        i4 = offerDAO.findOffersByRangeOfPrice(900d, 2600d).size();
        i5 = offerDAO.findOffersByRangeOfPrice(1001d, 2600d).size();

        Offer offer = new Offer();
        offer.setName("FindAllOffersInRangeOfPrice_offer1");

        Category category = new Category();
        category.setCategory("FindAllOffersInRangeOfPrice_category1");

        Price price = new Price();
        price.setPrice(1000d);

        offer.setCategory(category);
        offer.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("FindAllOffersInRangeOfPrice_tag1");
        offer.addTag(tag);

        tag = new Tag();
        tag.setTagname("FindAllOffersInRangeOfPrice_tag2");
        offer.addTag(tag);

        offer = offerDAO.create(offer);

        category = offer.getCategory();

        Offer offer1 = new Offer();
        offer1.setName("FindAllOffersInRangeOfPrice_offer1");

        price = new Price();
        price.setPrice(2500d);

        offer1.setCategory(category);
        offer1.setPrice(price);

        tag = new Tag();
        tag.setTagname("FindAllOffersInRangeOfPrice_tag3");
        offer1.addTag(tag);

        offerDAO.create(offer1);

        assertEquals(0 + i1, offerDAO.findOffersByRangeOfPrice(1500d, 1900d).size());
        assertEquals(1 + i2, offerDAO.findOffersByRangeOfPrice(1000d, 1000d).size());
        assertEquals(1 + i3, offerDAO.findOffersByRangeOfPrice(900d, 1100d).size());
        assertEquals(2 + i4, offerDAO.findOffersByRangeOfPrice(900d, 2600d).size());
        assertEquals(1 + i5, offerDAO.findOffersByRangeOfPrice(1001d, 2600d).size());


    }
}
