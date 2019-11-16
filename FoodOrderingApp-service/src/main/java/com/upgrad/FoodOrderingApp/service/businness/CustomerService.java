package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.UserDao;
import com.upgrad.FoodOrderingApp.service.entity.FullAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    @Autowired
    UserDao userDao;


    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity getCustomerViaContact(String contact){
        CustomerEntity entity = userDao.getCustomerViaContact(contact);
        return entity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity createCustomer(CustomerEntity entity){
        return userDao.createCustomer(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity getCustomerViaId(Integer id){
        CustomerEntity entity = userDao.getCustomerViaId(id);
        return entity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public FullAddressEntity createAddress(FullAddressEntity fullAddressEntity){
        return userDao.createAddress(fullAddressEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomer(CustomerEntity customer){
        userDao.updateCustomer(customer);
    }
}
