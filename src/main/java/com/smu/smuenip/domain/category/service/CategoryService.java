package com.smu.smuenip.domain.category.service;

import com.smu.smuenip.domain.category.entity.Category;
import com.smu.smuenip.domain.category.entity.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Category getCategoryByCategoryName(String categoryName) {
        return categoryRepository.findCategoryByCategoryName(categoryName)
            .orElse(null);
    }
}
