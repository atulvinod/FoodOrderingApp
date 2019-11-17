package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;

@Entity
@Table(name="restaurant",schema = "public")
@NamedQueries({
        @NamedQuery(name="getRestaurantViaName",query="select u from RestaurantEntity u where u.restaurantName like :restaurantName"),
        @NamedQuery(name="getRestaurantViaId",query="select u from RestaurantEntity u where u.id=:id"),
        @NamedQuery(name="getRestaurantViaUuid",query="select u from RestaurantEntity u where u.uuid=:uuid"),
        @NamedQuery(name="getRestaurantViaAddressId",query = "select u from RestaurantEntity u where u.addressId=:addressId")
})
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="uuid")
    private String uuid;

    @Column(name="restaurant_name")
    private String restaurantName;

    @Column(name="photo_url")
    private String photoUrl;

    @Column(name="customer_rating")
    private Integer customerRating;

    @Column(name="average_price_for_two")
    private Integer averagePriceForTwo;

    @Column(name="number_of_customers_rated")
    private Integer numberOfCustomersRated;

    @Column(name="address_id")
    private Integer addressId;

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

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Integer getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(Integer customerRating) {
        this.customerRating = customerRating;
    }

    public Integer getAveragePriceForTwo() {
        return averagePriceForTwo;
    }

    public void setAveragePriceForTwo(Integer averagePriceForTwo) {
        this.averagePriceForTwo = averagePriceForTwo;
    }

    public Integer getNumberOfCustomersRated() {
        return numberOfCustomersRated;
    }

    public void setNumberOfCustomersRated(Integer numberOfCustomersRated) {
        this.numberOfCustomersRated = numberOfCustomersRated;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }
}
