package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;

@Entity
@Table(name="restaurant_item",schema="public")
@NamedQueries({
        @NamedQuery(name="getRestaurantItemViaItemId",query="select u from RestaurantItemEntity u where u.itemId=:itemId"),
        @NamedQuery(name="getRestaurantItemViaId",query = "select u from RestaurantItemEntity u where u.id=:id"),
        @NamedQuery(name="getRestaurantItemViaRestaurantId",query="select u from RestaurantItemEntity u where u.restaurantId=:restaurantId"),

})
public class RestaurantItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name="item_id")
    private Integer itemId;

    @Column(name="restaurant_id")
    private Integer restaurantId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }
}
