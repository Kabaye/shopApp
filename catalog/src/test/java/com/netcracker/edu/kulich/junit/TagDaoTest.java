package com.netcracker.edu.kulich.junit;

import com.netcracker.edu.kulich.dao.OfferDAO;
import com.netcracker.edu.kulich.dao.OfferDAOManager;
import com.netcracker.edu.kulich.dao.TagDAO;
import com.netcracker.edu.kulich.dao.TagDAOManager;
import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Price;
import com.netcracker.edu.kulich.entity.Tag;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class TagDaoTest {
    private TagDAO tagDAO = new TagDAOManager();
    private OfferDAO offerDAO = new OfferDAOManager();

    @Test
    public void testCreateAndReadOneTag() {
        Tag tag = new Tag();
        tag.setTagname("tag1");
        tagDAO.create(tag);

        Tag tag1 = tagDAO.read(tag.getId());
        assertNotNull(tag1);
        assertEquals(tag.toString(), tag1.toString());
    }

    @Test
    public void testCreateWithSomeAmountOfTags() {
        Set<Tag> tags = new HashSet<>();

        Tag tag = new Tag();
        tag.setTagname("tag1");
        tags.add(tag);

        tag = new Tag();
        tag.setTagname("cat2");
        tags.add(tag);

        tag = new Tag();
        tag.setTagname("cat3");
        tags.add(tag);

        tagDAO.create(tags);

        StringBuffer buffer = new StringBuffer();
        StringBuffer expectedBuffer = new StringBuffer();
        for (Tag elem : tags) {
            buffer.append(tagDAO.read(elem.getId()));
            expectedBuffer.append(elem);
        }
        assertEquals(expectedBuffer.toString(), buffer.toString());
    }

    @Test
    public void testUpdateTag() {
        Tag tag = new Tag();
        tag.setTagname("tag1");

        tagDAO.create(tag);

        tag.setTagname("new tag1");

        tagDAO.update(tag);

        Tag tag1 = tagDAO.read(tag.getId());
        assertNotNull(tag1);
        assertEquals(tag.toString(), tag1.toString());
    }

    @Test
    public void testDeleteTag() {
        Tag tag = new Tag();
        tag.setTagname("tag1");

        tagDAO.create(tag);

        tagDAO.delete(tag.getId());

        Tag tag1 = tagDAO.read(tag.getId());

        assertNull(null, tag1);
    }

    @Test
    public void testDeleteTagCreatedWithOffer() {

        List<Offer> offers = offerDAO.findAll();

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

        StringBuilder builder = new StringBuilder();
        StringBuilder expectedBuilder = new StringBuilder();

        for (int i = 0; i < offers.size(); i++) {
            builder.append(offers1.get(i).getId());
            builder.append(offers1.get(i).getCategory().getId());
            expectedBuilder.append(offers.get(i).getId());
            expectedBuilder.append(offers.get(i).getCategory().getId());
            builder.append(offers1.get(i).getTags().size());
            expectedBuilder.append(offers.get(i).getTags().size());
        }
        assertEquals(expectedBuilder.toString(), builder.toString());
    }
}
