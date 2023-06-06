package com.smu.smuenip.domain.category.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Category {

    @Id
    private Long categoryId;

    @Column
    private String categoryName;

    @Column(name = "category_image")
    private String categoryImage;

    @Column(name = "category_image_url")
    private String recycleImage;
}
