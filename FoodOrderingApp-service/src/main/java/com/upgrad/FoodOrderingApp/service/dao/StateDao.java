package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class StateDao {

    @PersistenceContext
    private EntityManager entityManager;
    public StateEntity getState(String uuid){
        try{
            return entityManager.createNamedQuery("getState", StateEntity.class).setParameter("uuid",uuid).getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    public StateEntity getStateViaId(String id){
        try{
            return entityManager.createNamedQuery("getStateViaId", StateEntity.class).setParameter("id",Integer.parseInt(id)).getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }
}
