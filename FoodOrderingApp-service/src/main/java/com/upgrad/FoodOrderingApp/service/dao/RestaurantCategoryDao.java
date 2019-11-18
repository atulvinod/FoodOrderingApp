package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantCategoryDao {
    @PersistenceContext
    private EntityManager entityManager;

    public RestaurantCategoryEntity getRestaurantCategoryViaRestaurantId(Integer id){
        try{
            return entityManager.createNamedQuery("getRestaurantCategoryViaRestaurantId",RestaurantCategoryEntity.class).setParameter("restaurantId",id).getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public List<RestaurantCategoryEntity> getRestaurantCategoryViaCategoryId(Integer id){
        try{
            return entityManager.createNamedQuery("getRestaurantCategoryViaCategoryId",RestaurantCategoryEntity.class).setParameter("id",id).getResultList();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public List<RestaurantCategoryEntity> getCategoryViaRestaurantId(Integer id){
        try{
            return entityManager.createNamedQuery("getCategoriesViaRestaurantId",RestaurantCategoryEntity.class).setParameter("restaurantId",id).getResultList();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
