package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryItemDao {
    @PersistenceContext
    private EntityManager entityManager;

    public CategoryItemEntity getCategoryItemViaItemId(Integer id){
        try{
            return entityManager.createNamedQuery("getCategoryItemViaItemId",CategoryItemEntity.class).setParameter("itemId",id).getSingleResult();
        }catch (Exception e){
            return null;
        }
    }
    public List<CategoryItemEntity> getCategoryItemViaCategoryId(Integer id){
        try{
            return entityManager.createNamedQuery("getCategoryItemViaCategoryId",CategoryItemEntity.class).setParameter("categoryId",id).getResultList();
        }catch (Exception e){
            return null;
        }
    }
}
