package com.smu.smuenip.domain.category.service;

import com.smu.smuenip.domain.category.model.Category;
import com.smu.smuenip.domain.category.model.CategoryRepositorySupport;
import com.smu.smuenip.infrastructure.util.naver.ItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepositorySupport categoryRepository;

    @Transactional(readOnly = true)
    public Category findCategory(ItemDto itemDto) {

        if (itemDto == null)
            return null;

        return categoryRepository.findCategoryByDetailCategoryOrSubCategory(
                itemDto.getItems().get(0).getCategory4(),
                itemDto.getItems().get(0).getCategory3());

    }
}
