package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.SaveAddressRequest;
import com.upgrad.FoodOrderingApp.api.model.SaveAddressResponse;
import com.upgrad.FoodOrderingApp.service.businness.AuthenticationService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.StateService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.apache.tomcat.jni.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AddressController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private StateService stateService;

    @RequestMapping(method = RequestMethod.POST,path="/address",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestHeader("authorization") String authorization, final SaveAddressRequest addressRequest) throws AuthorizationFailedException, AddressNotFoundException, SaveAddressException {
        CustomerAuthTokenEntity authTokenEntity = authenticationService.getToken(authorization);
        if(authTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in");
        }
        if(authTokenEntity.getLogoutAt()!= null){
            if(authTokenEntity.getLogoutAt().isAfter(ZonedDateTime.now()))
                throw new AuthorizationFailedException("ATHR-002","Customer is logged out, Log in again to access this endpoint");
            else if(authTokenEntity.getExpiresAt().isAfter(ZonedDateTime.now()))
                 throw new AuthorizationFailedException("ATHR-003","Your session is expired. Log in again to access this endpoint.");

        }

        if(addressRequest.getFlatBuildingName()=="" ||
          addressRequest.getPincode()==""||
          addressRequest.getCity()==""||
          addressRequest.getStateUuid()==""
          ){
            throw new SaveAddressException("SAR-001","No field can be empty");
        }
        //Check if the pincode is valid
        try{
            if(addressRequest.getPincode().length()!=6){
                throw new Exception();
            }
            Long.parseLong(addressRequest.getPincode());
        }catch (Exception e){
            throw new SaveAddressException("SAR-002","Invalid pincode");
        }


        StateEntity stateEntity = stateService.getState(addressRequest.getStateUuid());
        if(stateEntity==null){
            throw new AddressNotFoundException("ANF-001","No state by this id");
        }

        AddressEntity address = new AddressEntity();

        address.setUuid(UUID.randomUUID().toString());
        address.setCity(addressRequest.getCity());
        address.setFlatBuilNumber(addressRequest.getFlatBuildingName());
        address.setLocality(addressRequest.getLocality());
        address.setPincode(addressRequest.getPincode());
        address.setStateId(stateEntity.getId());

        AddressEntity createdAddress = customerService.createAddress(address);
        SaveAddressResponse response = new SaveAddressResponse();
        response.setId(createdAddress.getUuid());
        response.setStatus("ADDRESS SUCCESSFULLY REGISTERED");

        return new ResponseEntity<SaveAddressResponse>(response,HttpStatus.OK);


    }
}
