package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;

@Entity
@Table(name="category_item")
@NamedQueries({
        @NamedQuery(name="getCategoryItemViaItemId",query="select u from CategoryItemEntity u where u.itemId=:itemId"),
        @NamedQuery(name="getCategoryItemViaCategoryId",query="select u from CategoryItemEntity u where u.categoryId=:categoryId")

})
public class CategoryItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name="item_id")
    private Integer itemId;

    @Column(name="category_id")
    private Integer categoryId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer uuid) {
        this.itemId = uuid;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
