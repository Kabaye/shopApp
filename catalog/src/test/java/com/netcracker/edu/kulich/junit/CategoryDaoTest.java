package com.netcracker.edu.kulich.junit;

import com.netcracker.edu.kulich.dao.CategoryDAO;
import com.netcracker.edu.kulich.dao.CategoryDAOImplementation;
import com.netcracker.edu.kulich.dao.OfferDAO;
import com.netcracker.edu.kulich.dao.OfferDAOImplementation;
import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Price;
import com.netcracker.edu.kulich.entity.Tag;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CategoryDaoTest {
    private CategoryDAO categoryDAO = new CategoryDAOImplementation();
    private OfferDAO offerDAO = new OfferDAOImplementation();

    @Test
    public void testCreateAndReadOneCategoryWithoutOffer() {
        Category category = new Category();

        category.setCategory("cat1");

        categoryDAO.create(category);

        Category category1 = categoryDAO.read(category.getId());
        assertNotNull(category1);
        assertEquals(category.toString(), category1.toString());
    }

    @Test
    public void testCreateAndReadWithTwoCreatedCategories() {
        Category category = new Category();
        category.setCategory("cat1");
        categoryDAO.create(category);

        category = new Category();
        category.setCategory("cat2");
        categoryDAO.create(category);

        Category category1 = categoryDAO.read(category.getId());

        assertNotNull(category1);
        assertEquals(category1.toString(), category.toString());
    }

    @Test
    public void testCreateWithSomeAmountOfCategories() {
        Set<Category> categories = new HashSet<>();

        Category category = new Category();
        category.setCategory("cat1");
        categories.add(category);

        category = new Category();
        category.setCategory("cat2");
        categories.add(category);

        category = new Category();
        category.setCategory("cat3");
        categories.add(category);

        categoryDAO.create(categories);

        StringBuffer buffer = new StringBuffer();
        StringBuffer expectedBuffer = new StringBuffer();
        for (Category elem : categories) {
            buffer.append(categoryDAO.read(elem.getId()));
            expectedBuffer.append(elem);
        }
        assertEquals(expectedBuffer.toString(), buffer.toString());
    }

    @Test
    public void testUpdateCategory() {
        Category category = new Category();

        category.setCategory("cat1");

        categoryDAO.create(category);

        category.setCategory("new cat1");

        categoryDAO.update(category);

        Category category1 = categoryDAO.read(category.getId());
        assertNotNull(category1);
        assertEquals(category.toString(), category1.toString());
    }

    @Test
    public void testDeleteCategory() {
        Category category = new Category();

        category.setCategory("cat1");

        categoryDAO.create(category);

        categoryDAO.delete(category.getId());

        Category category1 = categoryDAO.read(category.getId());
        assertEquals(null, category1);
    }

    @Test
    public void testDeleteCategoryWithOffers() {
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

        offerDAO.create(offer);

        categoryDAO.delete(category.getId());

        assertEquals(offers.toString(), offerDAO.findAll().toString());
    }
}
