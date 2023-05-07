package com.smu.smuenip.domain.Category.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, CustomCategoryRepository {

    @Override
    Optional<Category> findCategoryByCategoryId(Long categoryId);

    @Override
    Category findCategoryByDetailCategory(String detailCategory);

    @Override
    Category findCategoryByDetailCategoryOrSubCategory(String detailCategory, String subCategory);
}
