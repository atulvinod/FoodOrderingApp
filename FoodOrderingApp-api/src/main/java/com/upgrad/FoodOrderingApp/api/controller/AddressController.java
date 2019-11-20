package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.AuthenticationService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.StateService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.FullAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AddressController {


    //Import the services required for the functionality
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private StateService stateService;

    @Autowired
    private AddressService addressService;


    @RequestMapping(method = RequestMethod.POST,path="/address",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestHeader("authorization") String authorization, final SaveAddressRequest addressRequest) throws AuthorizationFailedException, AddressNotFoundException, SaveAddressException {
        CustomerAuthTokenEntity authTokenEntity = authenticationService.getToken(authorization);
        //Check if the customer is not logged in and check if the token hasnt expired
        if(authTokenEntity == null){
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in");
        }
        if(authTokenEntity.getLogoutAt()!= null){
            if(authTokenEntity.getLogoutAt().isAfter(ZonedDateTime.now()))
                throw new AuthorizationFailedException("ATHR-002","Customer is logged out, Log in again to access this endpoint");
            else if(authTokenEntity.getExpiresAt().isAfter(ZonedDateTime.now()))
                 throw new AuthorizationFailedException("ATHR-003","Your session is expired. Log in again to access this endpoint.");

        }

        //Check if any of the feilds are empty
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

        //get the state
        StateEntity stateEntity = stateService.getState(addressRequest.getStateUuid());
        if(stateEntity==null){
            throw new AddressNotFoundException("ANF-001","No state by this id");
        }

        //Create a full address entity
        FullAddressEntity address = new FullAddressEntity();

        //Set the feilds of the entity
        address.setUuid(UUID.randomUUID().toString());
        address.setCity(addressRequest.getCity());
        address.setFlatBuilNumber(addressRequest.getFlatBuildingName());
        address.setLocality(addressRequest.getLocality());
        address.setPincode(addressRequest.getPincode());
        address.setStateId(stateEntity.getId());

        //Fetch the persited entity form the service
        FullAddressEntity createdAddress = customerService.createAddress(address);
        SaveAddressResponse response = new SaveAddressResponse();
        response.setId(createdAddress.getUuid());
        response.setStatus("ADDRESS SUCCESSFULLY REGISTERED");

        //Create a customer Address Entity
        CustomerAddressEntity customerAddressEntity = new CustomerAddressEntity();
        customerAddressEntity.setCustomerId(authTokenEntity.getCustomerId());
        customerAddressEntity.setAddressId(createdAddress.getId());
        addressService.createCustomerAddress(customerAddressEntity);

        return new ResponseEntity<SaveAddressResponse>(response,HttpStatus.OK);


    }

    @RequestMapping(method=RequestMethod.DELETE,path="/address/{address_uuid}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeleteAddressResponse> deleteAddress(@RequestHeader("authorization") String authorization,final String addressUuid) throws AuthorizationFailedException {
        CustomerAuthTokenEntity token = authenticationService.getToken(authorization);
        //Check if the token is valid
        if(token == null){
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in");
        }
        if(token.getLogoutAt()!= null){
            if(token.getLogoutAt().isAfter(ZonedDateTime.now()))
                throw new AuthorizationFailedException("ATHR-002","Customer is logged out, Log in again to access this endpoint");
            else if(token.getExpiresAt().isAfter(ZonedDateTime.now()))
                throw new AuthorizationFailedException("ATHR-003","Your session is expired. Log in again to access this endpoint.");

        }

        FullAddressEntity fullAddress = addressService.getFullAddressViaAddressUuid(addressUuid);
        Integer fullAddressId = fullAddress.getId();

        CustomerAddressEntity customerAddress = addressService.getCustomerAddressviaAddressId(fullAddressId.toString());
        Integer customerId = customerAddress.getCustomerId();

        if(token.getCustomerId()!=customerId){
            throw new AuthorizationFailedException("ATHR-004","You are not authorized to view/update/delete any one else's address");
        }

        addressService.deleteFullAddress(fullAddress);
        DeleteAddressResponse response = new DeleteAddressResponse();
        response.setId(UUID.fromString(fullAddress.getUuid()));
        response.setStatus("ADDRESS DELETED SUCCESSFULLY");


        return new ResponseEntity<DeleteAddressResponse>(response,HttpStatus.OK);


    }

    @RequestMapping(method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE,path="/address/customer")
    public ResponseEntity<AddressListResponse> getAllAddress(@RequestHeader("authorization") String authorization) throws AuthorizationFailedException {
        CustomerAuthTokenEntity token = authenticationService.getToken(authorization);

        if(token == null){
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in");
        }
        if(token.getLogoutAt()!= null){
            if(token.getLogoutAt().isAfter(ZonedDateTime.now()))
                throw new AuthorizationFailedException("ATHR-002","Customer is logged out, Log in again to access this endpoint");
            else if(token.getExpiresAt().isAfter(ZonedDateTime.now()))
                throw new AuthorizationFailedException("ATHR-003","Your session is expired. Log in again to access this endpoint.");

        }

        AddressListResponse response = new AddressListResponse();
        List<AddressList> addressList = new ArrayList<>();

        List<CustomerAddressEntity> customerAddressEntities = addressService.getAllCustomerAddresses(token.getCustomerId().toString());
        //Get the addresses that are uploaded by the customer, convert them into the objects that are required for reponse
        for(CustomerAddressEntity e:customerAddressEntities){

            AddressList address = new AddressList();

            //Get the full address
            FullAddressEntity fullAddressEntity = addressService.getFullAddressViaAddressId(e.getAddressId().toString());
            address.setId(UUID.fromString(fullAddressEntity.getUuid()));
            address.setPincode(fullAddressEntity.getPincode());
            address.setLocality(fullAddressEntity.getLocality());
            address.setFlatBuildingName(fullAddressEntity.getFlatBuilNumber());
            address.setCity(fullAddressEntity.getCity());

            StateEntity stateEntity = stateService.getStateViaId(fullAddressEntity.getStateId().toString());
            AddressListState state = new AddressListState();
            state.setStateName(stateEntity.getStateName());
            state.setId(UUID.fromString(stateEntity.getUuid()));

            address.setState(state);

            addressList.add(address);
        }

        response.setAddresses(addressList);
        return new ResponseEntity<AddressListResponse>(response,HttpStatus.OK);

        }
}
