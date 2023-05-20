package com.smu.smuenip.domain.category.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Category {

    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column
    private String largeCategory;

    @Column
    private String middleCategory;

    @Column
    private String subCategory;

    @Column
    private String detailCategory;

    @Builder
    public Category(String largeCategory, String middleCategory, String subCategory,
        String detailCategory) {
        this.largeCategory = largeCategory;
        this.middleCategory = middleCategory;
        this.subCategory = subCategory;
        this.detailCategory = detailCategory;
    }
}
