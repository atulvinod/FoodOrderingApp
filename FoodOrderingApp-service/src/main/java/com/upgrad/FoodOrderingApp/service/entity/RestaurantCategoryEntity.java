package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;

@Entity
@Table(name="restaurant_category",schema = "public")
@NamedQueries({
        @NamedQuery(name="getRestaurantCategoryViaRestaurantId",query="select u from RestaurantCategoryEntity u where u.restaurantId=:id"),
        @NamedQuery(name="getRestaurantCategoryViaCategoryId",query="select u from RestaurantCategoryEntity u where u.categoryId=:id"),
        @NamedQuery(name="getCategoriesViaRestaurantId",query="select u from RestaurantCategoryEntity u where u.restaurantId=:restaurantId")

})
public class RestaurantCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name="restaurant_id")
    private Integer restaurantId;

    @Column(name="category_id")
    private Integer categoryId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
