package com.smu.smuenip.domain.receipt;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smu.smuenip.domain.Category.model.Category;
import com.smu.smuenip.domain.Category.model.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    EntityManager em;
    JPAQueryFactory jpaQueryFactory;

    @Autowired
    CategoryRepository categoryRepository;


    @Test
    void 조회_테스트() {
        Category category1 = new Category("짜장", "짬뽕", "치킨", "만두");
        categoryRepository.save(category1);

        Category category = categoryRepository.findCategoryByDetailCategory("만두");

        Assertions.assertThat(category.getLargeCategory()).isEqualTo("짜장");
    }

    @Test
    void 조회_테스트2() {
        Category category = new Category("커피1", "커피2", "커피3", "커피4");
        categoryRepository.save(category);
        Category category2 = new Category("커피1", "커피2", "커피3", null);

        categoryRepository.save(category2);
        Category result1 = categoryRepository.findCategoryByDetailCategory("커피4");

        Assertions.assertThat(result1.getCategoryId()).isEqualTo(1L);
        Assertions.assertThat(result1.getSubCategory()).isEqualTo("커피3");
        Assertions.assertThat(result1.getDetailCategory()).isEqualTo("커피4");
    }

}
