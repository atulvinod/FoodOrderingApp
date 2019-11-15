package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.CustomerNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;


@Repository
public class UserDao {
    @PersistenceContext
    EntityManager entityManager;


    public CustomerEntity createCustomer(CustomerEntity customerEntity){
        entityManager.persist(customerEntity);
        return customerEntity;

    }
    public CustomerEntity getCustomerViaContact(final String contactNumber){
        try{
            return entityManager.createNamedQuery("userByContact", CustomerEntity.class).setParameter("contact_number",contactNumber).getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }
    public CustomerEntity getCustomerViaId(final Integer id){
        try{
            return entityManager.createNamedQuery("userById", CustomerEntity.class).setParameter("id",id).getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }


}
