package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StateService {
    @Autowired
    private StateDao dao;
    @Transactional(propagation = Propagation.REQUIRED)
    public StateEntity getState(String uuid){
        return dao.getState(uuid);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public StateEntity getStateViaId(String id){
        return dao.getStateViaId(id);
    }


}
