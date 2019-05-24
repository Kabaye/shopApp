package com.netcracker.edu.kulich.category.dto.transformator;

import com.netcracker.edu.kulich.category.dto.CategoryDTO;
import com.netcracker.edu.kulich.category.entity.Category;
import com.netcracker.edu.kulich.dto.common.PairIdNameDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CategoryTransformator {

    public Category convertToEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setCategory(categoryDTO.getName());
        category.setId(categoryDTO.getId());
        return category;
    }

    public CategoryDTO convertToDto(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getCategory());
        categoryDTO.setOffers(category.getOffers().stream()
                .map(offer -> new PairIdNameDTO(offer.getId(), offer.getName()))
                .collect(Collectors.toSet()));
        return categoryDTO;
    }
}
