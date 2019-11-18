package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    public CategoryEntity getCategoryViaId(Integer id){
        try{
            return entityManager.createNamedQuery("getCategoryViaId",CategoryEntity.class).setParameter("id",id).getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public CategoryEntity getCategoryViaUuid(String id){
        try{
            return entityManager.createNamedQuery("getCategoryViaUuid",CategoryEntity.class).setParameter("uuid",id).getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public List<CategoryEntity> getAllCategories(){
        try{
            return entityManager.createNamedQuery("getAllCategories").getResultList();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
