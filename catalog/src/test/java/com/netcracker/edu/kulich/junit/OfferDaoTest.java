package com.netcracker.edu.kulich.junit;

import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Price;
import com.netcracker.edu.kulich.entity.Tag;
import com.netcracker.edu.kulich.exception.service.OfferServiceException;
import com.netcracker.edu.kulich.exception.service.TagServiceException;
import com.netcracker.edu.kulich.service.OfferService;
import com.netcracker.edu.kulich.service.TagService;
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
    private OfferService offerService;
    @Autowired
    private TagService tagService;

    @Test
    public void testCreateAndReadTwoOffers() throws OfferServiceException {
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

        offer = offerService.saveOffer(offer);

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

        offer = offerService.saveOffer(offer);

        Offer offer2 = offerService.getOfferById(offer.getId());
        assertNotNull(offer);
        assertEquals(offer, offer2);
    }

    @Test
    public void testFindAllOffers() throws OfferServiceException {
        List<Offer> offerList = offerService.getAllOffers();

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

        offer = offerService.saveOffer(offer);

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

        offer = offerService.saveOffer(offer);

        offerList.add(offer);

        List<Offer> offers = offerService.getAllOffers();


        if (offers.size() != offerList.size())
            fail();
        else {
            for (int i = 0; i < offerList.size(); i++) {
                assertEquals(offerList.get(i), offers.get(i));
            }
        }
    }

    @Test
    public void testUpdateOffer() throws OfferServiceException {
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

        offer = offerService.saveOffer(offer);

        offer.setName("new UpdateOffer_offer1");
        offer.getPrice().setPrice(666d);
        offer.getCategory().setCategory("new UpdateOffer_category1");
        offer.getTags().clear();

        offerService.updateOffer(offer);

        Offer offer1 = offerService.getOfferById(offer.getId());
        assertNotNull(offer1);
        assertEquals(offer, offer1);
    }

    @Test
    public void testDeleteOffer() throws OfferServiceException {
        List<Offer> offerList = offerService.getAllOffers();

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

        offer = offerService.saveOffer(offer);

        offerService.deleteOfferById(offer.getId());

        List<Offer> offers = offerService.getAllOffers();

        if (offers.size() != offerList.size())
            fail();
        else {
            for (int i = 0; i < offerList.size(); i++) {
                assertEquals(offerList.get(i), offers.get(i));
            }
        }
    }

    @Test
    public void testFindAllOffersWithCategory() throws OfferServiceException {
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

        offer = offerService.saveOffer(offer);

        offerList.add(offer);

        //category = offer.getCategory();

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

        offer = offerService.saveOffer(offer);

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

        offerService.saveOffer(offer);

        List<Offer> offers = offerService.findOffersByCategory(category);

        assertEquals(offerList.size(), offers.size());
        for (int i = 0; i < offerList.size(); i++) {
            assertEquals(offerList.get(i), offers.get(i));
        }

    }

    @Test
    public void testFindAllOffersWithTags() throws OfferServiceException, TagServiceException {
        Tag[] tags = new Tag[6];

        for (int i = 0; i < 5; i++) {
            tags[i] = new Tag();
            tags[i].setTagname("FindAllOffersWithTags_tag" + (i + 1));
            tags[i] = tagService.saveTag(tags[i]);
        }
        tags[5] = null;

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

        offers[0] = offerService.saveOffer(offers[0]);
        category = offers[0].getCategory();

        //1,3,4,5

        offers[1] = new Offer();
        offers[1].setName("FindAllOffersWithTags_offer2");
        offers[1].setPrice(price);
        offers[1].setCategory(category);
        offers[1].addTag(tags[1]);
        offers[1].addTag(tags[3]);
        offers[1].addTag(tags[4]);
        offers[1].addTag(tags[5]);

        offerService.saveOffer(offers[1]);

        //2,3,4

        offers[2] = new Offer();
        offers[2].setName("FindAllOffersWithTags_offer3");
        offers[2].setPrice(price);
        offers[2].setCategory(category);
        offers[2].addTag(tags[2]);
        offers[2].addTag(tags[3]);
        offers[2].addTag(tags[4]);
        offerService.saveOffer(offers[2]);

        //0,4,5

        offers[3] = new Offer();
        offers[3].setName("FindAllOffersWithTags_offer4");
        offers[3].setPrice(price);
        offers[3].setCategory(category);
        offers[3].addTag(tags[0]);
        offers[3].addTag(tags[4]);
        offers[3].addTag(tags[5]);
        offerService.saveOffer(offers[3]);


        assertEquals(2, offerService.findOffersByTags(Arrays.asList(tags[0])).size());
        assertEquals(4, offerService.findOffersByTags(Arrays.asList(tags[4])).size());
        assertEquals(2, offerService.findOffersByTags(Arrays.asList(tags[0], tags[4])).size());
        assertEquals(1, offerService.findOffersByTags(Arrays.asList(tags[2], tags[3], tags[4])).size());
        assertEquals(1, offerService.findOffersByTags(Arrays.asList(tags[2], tags[3], tags[4])).size());
        assertEquals(2, offerService.findOffersByTags(Arrays.asList(tags[1], tags[4])).size());
    }

    @Test
    public void testFindAllOffersInRangeOfPrice() throws OfferServiceException {
        int i1, i2, i3, i4, i5;
        i1 = offerService.findOffersByRangeOfPrice(1500d, 1900d).size();
        i2 = offerService.findOffersByRangeOfPrice(1000d, 1000d).size();
        i3 = offerService.findOffersByRangeOfPrice(900d, 1100d).size();
        i4 = offerService.findOffersByRangeOfPrice(900d, 2600d).size();
        i5 = offerService.findOffersByRangeOfPrice(1001d, 2600d).size();

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

        offer = offerService.saveOffer(offer);

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

        offerService.saveOffer(offer1);

        assertEquals(0 + i1, offerService.findOffersByRangeOfPrice(1500d, 1900d).size());
        assertEquals(1 + i2, offerService.findOffersByRangeOfPrice(1000d, 1000d).size());
        assertEquals(1 + i3, offerService.findOffersByRangeOfPrice(900d, 1100d).size());
        assertEquals(2 + i4, offerService.findOffersByRangeOfPrice(900d, 2600d).size());
        assertEquals(1 + i5, offerService.findOffersByRangeOfPrice(1001d, 2600d).size());


    }

    @Test
    public void testCreateOfferWithExistentCategoryAndTag() throws OfferServiceException, TagServiceException {
        Offer offer = new Offer();
        offer.setName("CreateOfferWithExistentCategoryAndTag_offer1");

        Category category = new Category();
        category.setCategory("CreateOfferWithExistentCategoryAndTag_category1");

        Price price = new Price();
        price.setPrice(1950d);

        offer.setCategory(category);
        offer.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("CreateOfferWithExistentCategoryAndTag_tag1");
        offer.addTag(tag);

        tag = new Tag();
        tag.setTagname("CreateOfferWithExistentCategoryAndTag_tag2");
        offer.addTag(tag);

        offer = offerService.saveOffer(offer);

        Offer offer1 = new Offer();
        offer1.setName("CreateOfferWithExistentCategoryAndTag_offer2");

        price = new Price();
        price.setPrice(2500d);

        category = new Category();
        category.setCategory("CreateOfferWithExistentCategoryAndTag_category1");
        offer1.setCategory(category);

        offer1.setPrice(price);

        tag = new Tag();
        tag.setId(2);
        tag.setTagname("CreateOfferWithExistentCategoryAndTag_tag2");
        offer1.addTag(tag);

        tag = new Tag();
        tag.setId(3);
        tag.setTagname("CreateOfferWithExistentCategoryAndTag_tag1");
        offer1.addTag(tag);

        offer1.addTag(null);

        offer1 = offerService.saveOffer(offer1);

        offer1.getTags().remove(null);

        assertEquals(offer.getTags(), offerService.getOfferById(offer.getId()).getTags());
        assertEquals(offer1.getTags(), offerService.getOfferById(offer1.getId()).getTags());
        assertEquals(offer.getTags(), offerService.getOfferById(offer1.getId()).getTags());

        tagService.deleteTagById(((Tag) offer1.getTags().toArray()[0]).getId());
        tagService.deleteTagById(((Tag) offer1.getTags().toArray()[1]).getId());

        assertEquals(0, offerService.getOfferById(offer1.getId()).getTags().size());
        assertEquals(0, offerService.getOfferById(offer.getId()).getTags().size());

    }
}
