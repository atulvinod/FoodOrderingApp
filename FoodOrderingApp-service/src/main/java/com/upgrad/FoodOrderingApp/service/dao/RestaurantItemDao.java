package com.upgrad.FoodOrderingApp.service.dao;


import com.upgrad.FoodOrderingApp.service.entity.RestaurantItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class RestaurantItemDao {
    @PersistenceContext
    EntityManager entityManager;

    public RestaurantItemEntity getRestaurantItemViaItemId(Integer id){
        try{
            return entityManager.createNamedQuery("getRestaurantItemViaItemId",RestaurantItemEntity.class).setParameter("itemId",id).getSingleResult();
        }catch (Exception e){
            return null;
        }
    }

    public RestaurantItemEntity getRestaurantItemViaId(Integer id){
        try{
            return entityManager.createNamedQuery("getRestaurantItemViaId",RestaurantItemEntity.class).setParameter("id",id).getSingleResult();
        }catch (Exception e){
            return null;
        }
    }

    public RestaurantItemEntity getRestaurantItemViaRestaurantId(Integer id){
        try{
            return entityManager.createNamedQuery("getRestaurantItemViaRestaurantId",RestaurantItemEntity.class).setParameter("restaurantId",id).getSingleResult();
        }catch (Exception e){
            return null;
        }
    }
}
