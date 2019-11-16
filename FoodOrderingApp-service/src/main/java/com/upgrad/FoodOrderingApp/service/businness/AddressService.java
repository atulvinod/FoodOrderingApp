package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.FullAddressEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressService {

    @Autowired
    private AddressDao dao;

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAddressEntity createCustomerAddress(CustomerAddressEntity entity){
        return dao.createCustomerAddress(entity);
    }

    public FullAddressEntity getFullAddressViaAddressUuid(String addressUuid){
        return dao.getFullAddressViaAddressUuid(addressUuid);
    }

    public CustomerAddressEntity getCustomerAddressviaAddressId(String addressId){
        return dao.getCustomerAddressViaAddressId(addressId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteFullAddress(FullAddressEntity address){
        dao.deleteFullAddress(address);
    }

}
