package com.netcracker.edu.kulich.controller;

import com.netcracker.edu.kulich.dto.CategoryDTO;
import com.netcracker.edu.kulich.dto.transformator.CategoryTransformator;
import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.exception.controller.ControllerException;
import com.netcracker.edu.kulich.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {
    private CategoryTransformator categoryTransformator;
    private CategoryService categoryService;

    public CategoryController(CategoryTransformator categoryTransformator, CategoryService categoryService) {
        this.categoryTransformator = categoryTransformator;
        this.categoryService = categoryService;
    }

    @GetMapping(value = "/{id:[\\d]+}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable("id") Long id) {
        Category category = categoryService.getCategoryById(id);
        if (category == null) {
            throw new ControllerException("There is no category with id: \'" + id + "\'.");
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
        category = categoryService.updateCategoryByName(category);
        return new ResponseEntity<>(categoryTransformator.convertToDto(category), HttpStatus.OK);
    }
}
