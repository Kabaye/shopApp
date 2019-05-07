package com.netcracker.edu.kulich.controller;

import com.netcracker.edu.kulich.dto.CategoryDTO;
import com.netcracker.edu.kulich.dto.transformator.CategoryTransformator;
import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.exception.controller.CategoryControllerException;
import com.netcracker.edu.kulich.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {
    private final String GETTING_NOT_EXISTENT_TAG = "You try to get not existent category.";
    private final String EMPTY_BODY_SAVING_REQUEST = "You try to call \"save\" method with empty body.";
    @Autowired
    private CategoryTransformator categoryTransformator;
    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "/{id:[\\d]+}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable("id") Long id) {
        Category category = categoryService.getCategoryById(id);
        if (category == null) {
            throw new CategoryControllerException(GETTING_NOT_EXISTENT_TAG);
        }
        return new ResponseEntity<>(categoryTransformator.convertToDto(category), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> saveCategory(@RequestBody CategoryDTO categoryDTO) {
        Category category = categoryService.saveCategory(categoryTransformator.convertToEntity(categoryDTO));
        return new ResponseEntity<>(categoryTransformator.convertToDto(category), HttpStatus.CREATED);
    }

    @PostMapping(value = "/categories")
    public ResponseEntity<List<CategoryDTO>> saveCategories(@RequestBody List<CategoryDTO> categories) {
        if ((categories == null) || (categories.size() == 0)) {
            throw new CategoryControllerException(EMPTY_BODY_SAVING_REQUEST);
        }
        List<Category> categoryList = new ArrayList<>();
        categories.forEach(categoryDTO -> categoryList.add(categoryService.saveCategory(categoryTransformator.convertToEntity(categoryDTO))));
        List<CategoryDTO> categoryDTOList = categoryList.stream()
                .map(categoryTransformator::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(categoryDTOList, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id:[\\d]+}")
    public ResponseEntity deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategoryById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id:[\\d]+}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO, @PathVariable("id") Long id) {
        Category category = categoryTransformator.convertToEntity(categoryDTO);
        category.setId(id);
        category = categoryService.updateCategory(category);
        return new ResponseEntity<>(categoryTransformator.convertToDto(category), HttpStatus.OK);
    }
}
