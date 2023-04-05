package com.smu.smuenip.domain.receipt;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smu.smuenip.domain.Category.model.Category;
import com.smu.smuenip.domain.Category.model.CategoryRepository;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
    

}
