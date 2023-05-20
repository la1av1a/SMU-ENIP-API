package com.smu.smuenip.domain.category.model;

import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
class CustomCategoryRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void findCategoryByDetailCategoryAndSubCategory() {
        Category category = new Category("커피1", "커피2", "커피3", "커피4");
        categoryRepository.save(category);

        Category category1 = categoryRepository.findCategoryByDetailCategoryOrSubCategory("커피4",
            "커피3");

        Assertions.assertThat(category1.getSubCategory()).isEqualTo("커피3");
    }
}
