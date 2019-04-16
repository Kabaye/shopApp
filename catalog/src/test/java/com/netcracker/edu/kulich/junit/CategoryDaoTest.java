package com.netcracker.edu.kulich.junit;

import com.netcracker.edu.kulich.dao.CategoryDAO;
import com.netcracker.edu.kulich.dao.CategoryDAOManager;
import com.netcracker.edu.kulich.dao.OfferDAO;
import com.netcracker.edu.kulich.dao.OfferDAOManager;
import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Price;
import com.netcracker.edu.kulich.entity.Tag;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class CategoryDaoTest {
    private CategoryDAO categoryDAO = new CategoryDAOManager();
    private OfferDAO offerDAO = new OfferDAOManager();

    @Test
    public void testCreateAndReadWithTwoCreatedCategories() {
        Category category = new Category();
        category.setCategory("CreateAndReadWithTwoCreatedCategories_category1");
        categoryDAO.create(category);

        category = new Category();
        category.setCategory("CreateAndReadWithTwoCreatedCategories_category2");
        categoryDAO.create(category);

        Category category1 = categoryDAO.read(category.getId());

        assertNotNull(category1);
        assertEquals(category1.toString(), category.toString());
    }

    @Test
    public void testCreateWithSomeAmountOfCategories() {
        Set<Category> categories = new HashSet<>();

        Category category = new Category();
        category.setCategory("CreateWithSomeAmountOfCategories_category1");
        categories.add(category);

        category = new Category();
        category.setCategory("CreateWithSomeAmountOfCategories_category2");
        categories.add(category);

        category = new Category();
        category.setCategory("CreateWithSomeAmountOfCategories_category3");
        categories.add(category);

        categoryDAO.create(categories);

        for (Category elem : categories) {
            assertEquals(elem, categoryDAO.read(elem.getId()));
        }
    }

    @Test
    public void testUpdateCategory() {
        Category category = new Category();

        category.setCategory("UpdateCategory_category1");

        categoryDAO.create(category);

        category.setCategory("new UpdateCategory_category1");

        categoryDAO.update(category);

        Category category1 = categoryDAO.read(category.getId());
        assertNotNull(category1);
        assertEquals(category.toString(), category1.toString());
    }

    @Test
    public void testDeleteCategory() {
        Category category = new Category();

        category.setCategory("DeleteCategory_category1");

        categoryDAO.create(category);

        categoryDAO.delete(category.getId());

        Category category1 = categoryDAO.read(category.getId());

        assertNull(category1);
    }

    @Test
    public void testDeleteCategoryWithOffers() {
        List<Offer> offerList = offerDAO.findAll();

        Offer offer = new Offer();
        offer.setName("DeleteCategoryWithOffers_offer1");

        Category category = new Category();
        category.setCategory("DeleteCategoryWithOffers_category1");

        Price price = new Price();
        price.setPrice(2500d);

        offer.setCategory(category);
        offer.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("DeleteCategoryWithOffers_tag1");
        offer.addTag(tag);

        tag = new Tag();
        tag.setTagname("DeleteCategoryWithOffers_tag2");
        offer.addTag(tag);

        offer = offerDAO.create(offer);
        category = offer.getCategory();

        offer = new Offer();
        offer.setName("DeleteCategoryWithOffers_offer2");

        price = new Price();
        price.setPrice(2500d);

        offer.setCategory(category);
        offer.setPrice(price);

        offer = offerDAO.create(offer);

        categoryDAO.delete(offer.getCategory().getId());
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
