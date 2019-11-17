package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    public CategoryEntity getCategoryViaId(Integer id){
        try{
            return entityManager.createNamedQuery("getCategoryViaId",CategoryEntity.class).setParameter("id",id).getSingleResult();
        }catch (Exception e){
            return null;
        }
    }
    public CategoryEntity getCategoryViaUuid(String id){
        try{
            return entityManager.createNamedQuery("getCategoryViaUuid",CategoryEntity.class).setParameter("uuid",id).getSingleResult();
        }catch (Exception e){
            return null;
        }
    }
}
