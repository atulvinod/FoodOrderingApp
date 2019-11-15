package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class AuthTokenDao {
    @PersistenceContext
    EntityManager entityManager;

    public CustomerAuthTokenEntity createAuthToken(com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity authToken){
        entityManager.persist(authToken);
        return authToken;
    }

    public CustomerAuthTokenEntity checkToken(String authTokenEntity){
        try{
              return  entityManager.createNamedQuery("getToken",CustomerAuthTokenEntity.class).setParameter("token",authTokenEntity).getSingleResult();

        }catch(Exception e){
            return null;
        }
    }

    public void updateToken(CustomerAuthTokenEntity token){
        entityManager.merge(token);

    }
}

