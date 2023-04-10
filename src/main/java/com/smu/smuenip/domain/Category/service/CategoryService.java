package com.smu.smuenip.domain.Category.service;

import com.smu.smuenip.Infrastructure.util.naver.ItemVO;
import com.smu.smuenip.domain.Category.model.Category;
import com.smu.smuenip.domain.Category.model.CategoryRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepositorySupport categoryRepository;

    @Transactional(readOnly = true)
    public Category getCategory(ItemVO itemVO) {

        return categoryRepository.findCategoryByDetailCategoryOrSubCategory(
            itemVO.getItems().get(0).getCategory4(),
            itemVO.getItems().get(0).getCategory3()
        );
    }
}
