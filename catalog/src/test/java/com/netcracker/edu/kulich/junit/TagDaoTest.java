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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@Deprecated
@SpringBootTest
@RunWith(SpringRunner.class)
public class TagDaoTest {

    @Autowired
    private OfferService offerService;
    @Autowired
    private TagService tagService;

    @Test
    public void testCreateAndReadOneTag() throws TagServiceException {
        Tag tag = new Tag();
        tag.setTagname("CreateAndReadOneTag_tag1");
        tagService.saveTag(tag);

        Tag tag1 = tagService.getTagById(tag.getId());
        assertNotNull(tag1);
        assertEquals(tag.toString(), tag1.toString());
    }

    @Test
    public void testCreateWithSomeAmountOfTags() throws TagServiceException {
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

        tagService.saveTags(tags);

        for (Tag elem : tags) {
            assertEquals(elem, tagService.getTagById(elem.getId()));
        }
    }

    @Test
    public void testUpdateTag() throws TagServiceException {
        Tag tag = new Tag();
        tag.setTagname("UpdateTag_tag1");

        tagService.saveTag(tag);

        tag.setTagname("new UpdateTag_tag1");

        tagService.updateTag(tag);

        Tag tag1 = tagService.getTagById(tag.getId());
        assertNotNull(tag1);
        assertEquals(tag.toString(), tag1.toString());
    }

    @Test
    public void testDeleteTag() throws TagServiceException {
        Tag tag = new Tag();
        tag.setTagname("DeleteTag_tag1");

        tagService.saveTag(tag);

        tagService.deleteTagById(tag.getId());

        Tag tag1 = tagService.getTagById(tag.getId());

        assertNull(null, tag1);
    }

    @Test
    public void testDeleteTagCreatedWithOffer() throws OfferServiceException, TagServiceException {

        List<Offer> offers = offerService.getAllOffers();

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

        offer = offerService.saveOffer(offer);

        Iterator<Tag> tagIterator = offer.getTags().iterator();
        while (true) {
            Tag tag1 = tagIterator.next();
            if (tag1.getTagname().equals(tag.getTagname())) {
                tag = tag1;
                break;
            }
        }

        tagService.deleteTagById(tag.getId());

        Tag tag1 = tagService.getTagById(tag.getId());
        assertNull(tag1);

        List<Offer> offers1 = offerService.getAllOffers();

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

    @Test
    public void testCreateAndUpdateWithNotUniqueTag() throws TagServiceException {
        Tag tag = new Tag();
        tag.setTagname("CreateAndUpdateWithNotUniqueTag_tag1");
        tagService.saveTag(tag);

        Tag tag1 = new Tag();
        tag1.setTagname("CreateAndUpdateWithNotUniqueTag_tag1");

        try {
            tagService.saveTag(tag1);
        } catch (TagServiceException exc) {
            assertTrue(true);
        }

        tag1.setTagname("CreateAndUpdateWithNotUniqueTag_tag2");

        tagService.saveTag(tag1);

        tag1.setTagname("CreateAndUpdateWithNotUniqueTag_tag1");
        try {
            tagService.updateTag(tag1);
        } catch (TagServiceException exc) {
            assertTrue(true);
        }
    }

    @Test
    public void testFindByName() throws TagServiceException {
        String s1 = "FindByName_tag1", s2 = "FindByName_tag2";
        Tag tag = new Tag();
        tag.setTagname(s1);
        tagService.saveTag(tag);

        Tag tag1 = new Tag();
        tag1.setTagname(s2);
        tagService.saveTag(tag1);

        assertEquals(tag, tagService.getTagByName(s1));
        assertEquals(tag1, tagService.getTagByName(s2));

        assertNull(tagService.getTagByName("SPECIAL_TAGNAME_FOR_GETTING_NULL_FROM_GET_BY_TAGHAME_METHOD"));


    }
}