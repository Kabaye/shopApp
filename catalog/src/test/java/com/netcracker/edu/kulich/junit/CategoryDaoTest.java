package com.netcracker.edu.kulich.junit;

import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Price;
import com.netcracker.edu.kulich.entity.Tag;
import com.netcracker.edu.kulich.exception.service.CategoryServiceException;
import com.netcracker.edu.kulich.exception.service.OfferServiceException;
import com.netcracker.edu.kulich.service.CategoryService;
import com.netcracker.edu.kulich.service.OfferService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CategoryDaoTest {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OfferService offerService;

    @Test
    public void testCreateAndReadWithTwoCreatedCategories() throws CategoryServiceException {
        Category category = new Category();
        category.setCategory("CreateAndReadWithTwoCreatedCategories_category1");
        categoryService.saveCategory(category);

        category = new Category();
        category.setCategory("CreateAndReadWithTwoCreatedCategories_category2");
        categoryService.saveCategory(category);

        Category category1 = categoryService.getCategoryById(category.getId());

        assertNotNull(category1);
        assertEquals(category1.toString(), category.toString());
    }

    @Test
    public void testCreateWithSomeAmountOfCategories() throws CategoryServiceException {
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

        categoryService.saveCategories(categories);

        for (Category elem : categories) {
            assertEquals(elem, categoryService.getCategoryById(elem.getId()));
        }
    }

    @Test
    public void testUpdateCategory() throws CategoryServiceException {
        Category category = new Category();

        category.setCategory("UpdateCategory_category1");

        categoryService.saveCategory(category);

        category.setCategory("new UpdateCategory_category1");

        categoryService.updateCategory(category);

        Category category1 = categoryService.getCategoryById(category.getId());
        assertNotNull(category1);
        assertEquals(category.toString(), category1.toString());
    }

    @Test
    public void testDeleteCategory() throws CategoryServiceException {
        Category category = new Category();

        category.setCategory("DeleteCategory_category1");

        categoryService.saveCategory(category);

        categoryService.deleteCategoryById(category.getId());

        Category category1 = categoryService.getCategoryById(category.getId());

        assertNull(category1);
    }

    @Test
    public void testDeleteCategoryWithOffers() throws CategoryServiceException, OfferServiceException {
        List<Offer> offerList = offerService.getAllOffers();

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

        offer = offerService.saveOffer(offer);
        category = offer.getCategory();

        offer = new Offer();
        offer.setName("DeleteCategoryWithOffers_offer2");

        price = new Price();
        price.setPrice(2500d);

        offer.setCategory(category);
        offer.setPrice(price);

        offer = offerService.saveOffer(offer);

        categoryService.deleteCategoryById(offer.getCategory().getId());
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
    public void testCreateAndUpdateCategoryWithNotUniqueName() throws CategoryServiceException {
        Category category = new Category();
        category.setCategory("CreateAndUpdateCategoryWithNotUniqueName_category1");
        categoryService.saveCategory(category);

        category = new Category();
        category.setCategory("CreateAndUpdateCategoryWithNotUniqueName_category1");
        try {
            categoryService.saveCategory(category);
        } catch (CategoryServiceException exc) {
            if (exc.getMessage().toLowerCase().contains("ins"))
                assertTrue(true);
        }

        category.setCategory("CreateAndUpdateCategoryWithNotUniqueName_category2");

        categoryService.saveCategory(category);

        category.setCategory("CreateAndUpdateCategoryWithNotUniqueName_category1");
        try {
            categoryService.updateCategory(category);
        } catch (CategoryServiceException exc) {
            if (exc.getMessage().contains("upd"))
                assertTrue(true);
        }
    }

    @Test
    public void testGetCategoryByName() throws CategoryServiceException {

        String s1 = "GetCategoryByName_category1", s2 = "GetCategoryByName_category2";

        Category category = new Category();
        category.setCategory(s1);
        categoryService.saveCategory(category);

        Category category1 = new Category();
        category1.setCategory(s2);
        categoryService.saveCategory(category1);

        assertEquals(category, categoryService.getCategoryByName(s1));
        assertEquals(category1, categoryService.getCategoryByName(s2));
        assertNull(categoryService.getCategoryByName("SPECIAL_CATEGORY_FOR_NULL_RETURN"));
    }

}
