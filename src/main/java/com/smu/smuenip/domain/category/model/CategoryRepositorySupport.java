package com.smu.smuenip.domain.category.model;

import static com.smu.smuenip.domain.category.model.QCategory.category;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepositorySupport extends QuerydslRepositorySupport implements
    CustomCategoryRepository {

    private final JPAQueryFactory queryFactory;

    public CategoryRepositorySupport(JPAQueryFactory queryFactory) {
        super(Category.class);
        this.queryFactory = queryFactory;
    }


    @Override
    public Optional<Category> findCategoryByCategoryId(Long categoryId) {
        Category category1 = queryFactory.selectFrom(category)
            .where(category.categoryId.eq(1L))
            .fetchOne();

        return Optional.ofNullable(category1);
    }

    @Override
    public Category findCategoryByDetailCategory(String detailCategory) {
        return queryFactory.selectFrom(category)
            .where(category.detailCategory.eq(detailCategory))
            .fetchOne();
    }

    @Override
    public Category findCategoryByDetailCategoryOrSubCategory(String detailCategory,
        String subCategory) {
        Category categoryByDetail = queryFactory.selectFrom(category)
            .where(category.detailCategory.eq(detailCategory))
            .fetchOne();

        if (categoryByDetail == null) {
            return queryFactory.selectFrom(category)
                .where(category.subCategory.eq(subCategory))
                .fetchOne();
        }

        return categoryByDetail;
    }
}
