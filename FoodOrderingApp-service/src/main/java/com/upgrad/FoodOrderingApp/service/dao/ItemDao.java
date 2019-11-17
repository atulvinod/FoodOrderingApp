package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ItemDao {
    @PersistenceContext
    private EntityManager entityManager;

    public ItemEntity getItemViaId(Integer id){
        try{
            return entityManager.createNamedQuery("getItemViaId",ItemEntity.class).setParameter("id",id).getSingleResult();
        }catch (Exception e){
            return null;
        }
    }
}
