package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantDao {
    @PersistenceContext
    EntityManager entityManager;

    //Restaurant Entity
    public List<RestaurantEntity> getRestaurantViaName(String name){
        try{
            return entityManager.createNamedQuery("getRestaurantViaName",RestaurantEntity.class).setParameter("restaurantName",("%"+name+"%")).getResultList();
        }catch(Exception e){
            return null;
        }
    }

    public RestaurantEntity getRestaurantViaId(Integer id){
        try{
            return entityManager.createNamedQuery("getRestaurantViaId",RestaurantEntity.class).setParameter("id",id).getSingleResult();
        }catch (Exception e){
            return null;
        }
    }
    public RestaurantEntity getRestaurantViaUuid(String id){
        try{
            return entityManager.createNamedQuery("getRestaurantViaUuid",RestaurantEntity.class).setParameter("uuid",id).getSingleResult();
        }catch (Exception e){
            return null;
        }
    }
    public RestaurantEntity getRestaurantViaAddressId(Integer id){
        try{
            return entityManager.createNamedQuery("getRestaurantViaAddressId",RestaurantEntity.class).setParameter("addressId",id).getSingleResult();
        }catch (Exception e){
            return null;
        }
    }

}
