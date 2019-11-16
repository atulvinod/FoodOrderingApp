package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.FullAddressEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class AddressDao {
    @PersistenceContext
    EntityManager entityManager;

    public CustomerAddressEntity createCustomerAddress(CustomerAddressEntity entity){
        entityManager.persist(entity);
        return entity;
    }

    public FullAddressEntity getFullAddressViaAddressUuid(String AddressUuid){
        try{
            return entityManager.createNamedQuery("getFullAddressViaAddressUuid",FullAddressEntity.class).setParameter("uuid",AddressUuid).getSingleResult();
        }catch(Exception e){

        }
        return null;
    }
    public CustomerAddressEntity getCustomerAddressViaAddressId(String AddressId){
        try{
            return entityManager.createNamedQuery("getAddressViaAddressId",CustomerAddressEntity.class).setParameter("addressId",Integer.parseInt(AddressId)).getSingleResult();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public void deleteFullAddress(FullAddressEntity entity){
        entityManager.remove(entity);
    }

}
