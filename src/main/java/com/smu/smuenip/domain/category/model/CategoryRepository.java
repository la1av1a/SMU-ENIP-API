package com.smu.smuenip.domain.category.model;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>,
    CustomCategoryRepository {

    @Override
    Optional<Category> findCategoryByCategoryId(Long categoryId);

    @Override
    Category findCategoryByDetailCategory(String detailCategory);

    @Override
    Category findCategoryByDetailCategoryOrSubCategory(String detailCategory, String subCategory);
}
