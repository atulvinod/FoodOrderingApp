package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;

@Entity
@Table(name="customer_address",schema = "public")
@NamedQueries({
        @NamedQuery(name="getAddressViaAddressId",query = "select u from CustomerAddressEntity u where u.addressId=:id"),
        @NamedQuery(name="getAddressViaCustomerId",query = "select u from CustomerAddressEntity u where u.customerId=:customerId")
})
public class CustomerAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="customer_id")
    private Integer customerId;

    @Column(name="address_id")
    private Integer addressId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }
}
