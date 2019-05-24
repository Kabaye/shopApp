package com.netcracker.edu.kulich.category.controller;

import com.netcracker.edu.kulich.category.dto.CategoryDTO;
import com.netcracker.edu.kulich.category.dto.transformator.CategoryTransformator;
import com.netcracker.edu.kulich.category.entity.Category;
import com.netcracker.edu.kulich.category.service.CategoryService;
import com.netcracker.edu.kulich.exception.controller.ControllerException;
import com.netcracker.edu.kulich.logging.annotation.DefaultLogging;
import com.netcracker.edu.kulich.logging.annotation.Logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/categories")
@DefaultLogging
public class CategoryController {
    private CategoryTransformator categoryTransformator;
    private CategoryService categoryService;
    private Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    public CategoryController(CategoryTransformator categoryTransformator, CategoryService categoryService) {
        this.categoryTransformator = categoryTransformator;
        this.categoryService = categoryService;
    }

    @GetMapping(value = "/{id:[\\d]+}")
    @Logging(startMessage = "Request for getting category by id is received.", endMessage = "Response on request for getting category by id is sent.", startFromNewLine = true)
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable("id") Long id) {
        Category category = categoryService.getCategoryById(id);
        if (category == null) {
            logger.error("Category is not found.");
            throw new ControllerException("There is no category with id: \'" + id + "\'.");
        }
        return new ResponseEntity<>(categoryTransformator.convertToDto(category), HttpStatus.OK);
    }

    @PostMapping
    @Logging(startMessage = "Request for saving category is received.", endMessage = "Response on request for saving category is sent.", startFromNewLine = true)
    public ResponseEntity<CategoryDTO> saveCategory(@RequestBody CategoryDTO categoryDTO) {
        Category category = categoryService.saveCategory(categoryTransformator.convertToEntity(categoryDTO));
        return new ResponseEntity<>(categoryTransformator.convertToDto(category), HttpStatus.CREATED);
    }

    @PostMapping(value = "/categories")
    @Logging(startMessage = "Request for saving some categories is received.", endMessage = "Response on request for saving some categories is sent.", startFromNewLine = true)
    public ResponseEntity<List<CategoryDTO>> saveCategories(@RequestBody List<CategoryDTO> categories) {
        List<Category> categoryList = new ArrayList<>();
        categories.forEach(categoryDTO -> categoryList.add(categoryService.saveCategory(categoryTransformator.convertToEntity(categoryDTO))));
        List<CategoryDTO> categoryDTOList = categoryList.stream()
                .map(categoryTransformator::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(categoryDTOList, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id:[\\d]+}")
    @Logging(startMessage = "Request for deleting category is received.", endMessage = "Response on request for deleting category is sent.", startFromNewLine = true)
    public ResponseEntity deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategoryById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id:[\\d]+}")
    @Logging(startMessage = "Request for updating category is received.", endMessage = "Response on request for updating category is sent.", startFromNewLine = true)
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable("id") Long id) {
        Category category = categoryTransformator.convertToEntity(categoryDTO);
        category.setId(id);
        category = categoryService.updateCategoryByName(category);
        return new ResponseEntity<>(categoryTransformator.convertToDto(category), HttpStatus.OK);
    }
}
