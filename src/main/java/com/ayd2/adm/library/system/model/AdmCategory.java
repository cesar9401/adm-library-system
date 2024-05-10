package com.ayd2.adm.library.system.model;

import com.ayd2.adm.library.system.demo.enums.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "adm_category")
@Getter
@Setter
public class AdmCategory {

    @Id
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "parent_category_id")
    private Long parentCategoryId;

    @Column(name = "internal_id")
    private Long internalId;

    @Column(name = "category_description")
    private String description;

    @Column(name = "category_value_1")
    private String value1;

    public boolean is(Long otherInternalId) {
        return this.internalId.equals(otherInternalId);
    }

    public boolean is(AdmCategory other) {
        return this.is(other.internalId);
    }

    public boolean is(Category category) {
        return is(category.internalId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdmCategory that = (AdmCategory) o;

        return internalId.equals(that.internalId);
    }

    @Override
    public int hashCode() {
        return internalId.hashCode();
    }
}
