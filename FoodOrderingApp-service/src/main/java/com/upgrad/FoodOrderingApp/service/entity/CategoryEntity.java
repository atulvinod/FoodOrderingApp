package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;

@Entity
@Table(name="category",schema="public")
@NamedQueries({
        @NamedQuery(name="getCategoryViaId",query="select u from CategoryEntity u where u.id=:id"),
        @NamedQuery(name="getCategoryViaUuid",query="select u from CategoryEntity u where u.uuid=uuid")
})
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name="uuid")
    private String uuid;

    @Column(name="category_name")
    private String categoryName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
