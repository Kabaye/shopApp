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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TagDaoTest {
    @Autowired
    private OfferDAO offerDAO;
    @Autowired
    private TagDAO tagDAO;

    @Test
    public void testCreateAndReadOneTag() {
        Tag tag = new Tag();
        tag.setTagname("CreateAndReadOneTag_tag1");
        tagDAO.create(tag);

        Tag tag1 = tagDAO.read(tag.getId());
        assertNotNull(tag1);
        assertEquals(tag.toString(), tag1.toString());
    }

    @Test
    public void testCreateWithSomeAmountOfTags() {
        Set<Tag> tags = new HashSet<>();

        Tag tag = new Tag();
        tag.setTagname("CreateWithSomeAmountOfTags_tag1");
        tags.add(tag);

        tag = new Tag();
        tag.setTagname("CreateWithSomeAmountOfTags_tag2");
        tags.add(tag);

        tag = new Tag();
        tag.setTagname("CreateWithSomeAmountOfTags_tag3");
        tags.add(tag);

        tagDAO.create(tags);

        for (Tag elem : tags) {
            assertEquals(elem, tagDAO.read(elem.getId()));
        }
    }

    @Test
    public void testUpdateTag() {
        Tag tag = new Tag();
        tag.setTagname("UpdateTag_tag1");

        tagDAO.create(tag);

        tag.setTagname("new UpdateTag_tag1");

        tagDAO.update(tag);

        Tag tag1 = tagDAO.read(tag.getId());
        assertNotNull(tag1);
        assertEquals(tag.toString(), tag1.toString());
    }

    @Test
    public void testDeleteTag() {
        Tag tag = new Tag();
        tag.setTagname("DeleteTag_tag1");

        tagDAO.create(tag);

        tagDAO.delete(tag.getId());

        Tag tag1 = tagDAO.read(tag.getId());

        assertNull(null, tag1);
    }

    @Test
    public void testDeleteTagCreatedWithOffer() {

        List<Offer> offers = offerDAO.findAll();

        Offer offer = new Offer();
        offer.setName("DeleteTagCreatedWithOffer_offer1");

        Category category = new Category();
        category.setCategory("DeleteTagCreatedWithOffer_category1");

        Price price = new Price();
        price.setPrice(2500d);

        offer.setCategory(category);
        offer.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("DeleteTagCreatedWithOffer_tag1");
        offer.addTag(tag);

        tag = new Tag();
        tag.setTagname("DeleteTagCreatedWithOffer_tag2");
        offer.addTag(tag);

        offer = offerDAO.create(offer);

        Iterator<Tag> tagIterator = offer.getTags().iterator();
        while (true) {
            Tag tag1 = tagIterator.next();
            if (tag1.getTagname().equals(tag.getTagname())) {
                tag = tag1;
                break;
            }
        }
        tagDAO.delete(tag.getId());

        Tag tag1 = tagDAO.read(tag.getId());
        assertNull(tag1);

        List<Offer> offers1 = offerDAO.findAll();

        offer.getTags().remove(tag);
        offers.add(offer);
        if (offers.size() != offers1.size())
            fail();
        else {
            for (int i = 0; i < offers.size(); i++) {
                assertEquals(offers.get(i), offers1.get(i));
            }
        }
    }
}
