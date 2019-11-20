package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AuthenticationService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.PasswordCryptographyProvider;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.PasswordStrengthService;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class CustomerController {

    @Autowired
    private PasswordStrengthService passwordStrengthService;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(method = RequestMethod.POST,path = "/customer/signup",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupCustomerResponse> customerSignup(final  SignupCustomerRequest  signupCustomerRequest) throws SignUpRestrictedException {


        String contactNumber = signupCustomerRequest.getContactNumber();
        String email = signupCustomerRequest.getEmailAddress();
        String password = signupCustomerRequest.getPassword();
        String firstName = signupCustomerRequest.getFirstName();
        String lastName = signupCustomerRequest.getLastName();

        /**If any required field is empty, throw an error*/
        if(contactNumber.equals("") ||
            email.equals("")||
            password.equals("")||
            firstName.equals("")
        ){
            throw new SignUpRestrictedException("SGR-005","Except last name all fields should be filled");
        }

        /**If the contact number contains only numbers, it will be parsed to a long,
         * if it contains any character it will throw an illegal exception
         */
        try{
            Long.parseLong(contactNumber);
        }catch(Exception e){
            throw new SignUpRestrictedException("SGR-003","Invalid contact number!");
        }

        /**To check if the number length is less 10 digits*/
        if(contactNumber.length()<10){
            throw new SignUpRestrictedException("SGR-003","Invalid contact number!");
        }

        /**If the email is of incorrect format, throw an exception*/
        if(!email.contains("@")||!email.contains(".")){
            throw new SignUpRestrictedException("SGR-002","Invalid email format!");
        }

        /**If the password is weak and throw an exception
         */
        if(passwordStrengthService.checkPasswordStrength(password)==false){
            //TODO: Correct the Error Code
            throw new SignUpRestrictedException("USE","Weak Password!");
        }


        if(customerService.getCustomerViaContact(contactNumber)!=null){
            throw new SignUpRestrictedException("SGR-001","This contact number is already registered! Try other contact number.");
        }

        String userUuid = UUID.randomUUID().toString();

        CustomerEntity userEntity = new CustomerEntity();
        userEntity.setContact_number(contactNumber);
        userEntity.setEmail(email);
        userEntity.setFirstName(firstName);
        userEntity.setLastName(lastName);
        userEntity.setUuid(userUuid);

        //Encrypt the password
        String[] encrytedPassword = cryptographyProvider.encrypt(password);

        //Set the password to the user
        userEntity.setSalt(encrytedPassword[0]);
        userEntity.setPassword(encrytedPassword[1]);

        customerService.createCustomer(userEntity);
        SignupCustomerResponse response = new SignupCustomerResponse().id(userUuid).status("USER SUCCESSFULLY REGISTERED");

        return new ResponseEntity<SignupCustomerResponse>(response, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST,path = "/customer/login",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> customerLogin(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AuthenticationFailedException {

        /**Try to decode the Base64 header, if failed , then throw an Exception*/
        String[] decodedArray;
        try {
            byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
            String decodedText = new String(decode);

            // contactNumber:password
            decodedArray = decodedText.split(":");

        }catch (Exception e){
            throw new AuthenticationFailedException("ATH-003","Incorrect format of decoded customer name and password");
        }

        //Get the token from the authentication Service
        CustomerAuthTokenEntity token = authenticationService.authenticate(decodedArray[0],decodedArray[1]);

        //Get the entity from the token id
        CustomerEntity entity = customerService.getCustomerViaId(token.getCustomerId());

        //Create a new Response
        LoginResponse response  = new LoginResponse();
        response.setId(entity.getUuid());
        response.setContactNumber(entity.getContact_number());
        response.setFirstName(entity.getFirstName());
        response.setEmailAddress(entity.getEmail());
        response.setLastName(entity.getLastName());
        response.setMessage("LOGGED IN SUCCESSFULLY");


        //Create a new header
        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token",token.getAccessToken());
        return new ResponseEntity<LoginResponse>(response,headers,HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.POST , path ="/customer/logout",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LogoutResponse> customerLogout(@RequestHeader("authentication") final String authentication) throws AuthorizationFailedException {
        CustomerAuthTokenEntity token = authenticationService.getToken(authentication);
        if(token == null){
            throw new AuthorizationFailedException("ATHR-001","Customer not logged in");
        }
        ZonedDateTime now = ZonedDateTime.now();

        //If the user has already logged out, then throw an exception
        if(token.getLogoutAt() != null ){
            if(token.getLogoutAt().isBefore(now))
            throw new AuthorizationFailedException("ATHR-002","Customer is logged out. Log in again to access this endpoint");
        }

        //If the current time is greater than the expiry time of the token, then throw an exception
        if(now.isAfter(token.getExpiresAt())){
            throw new AuthorizationFailedException("ATHR-003","Your session is expired. Log in again to access this endpoint");
        }

        CustomerEntity customer = customerService.getCustomerViaId(token.getCustomerId());
        token.setLogoutAt(now);



        //Update the token
        authenticationService.updateToken(token);

        //Create a new Response
        LogoutResponse response = new LogoutResponse();
        response.setId(customer.getUuid());
        response.setMessage("LOGGED OUT SUCCESSFULLY");

        return new ResponseEntity<LogoutResponse>(response,HttpStatus.OK);

    }

    @RequestMapping(method=RequestMethod.PUT,path="/customer/password",produces=MediaType.APPLICATION_JSON_UTF8_VALUE,consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdatePasswordResponse> passwordUpdate(@RequestHeader("authorization") final String authorization,UpdatePasswordRequest request) throws AuthorizationFailedException, UpdateCustomerException {
        CustomerAuthTokenEntity authToken = authenticationService.getToken(authorization);
        if(authToken == null){
            throw new AuthorizationFailedException("ATHR-001","Customer is not logged in");
        }
        //Get the customer
        CustomerEntity customer = customerService.getCustomerViaId(authToken.getCustomerId());

        //To check if the token is expired
        if(authToken.getExpiresAt().isBefore(ZonedDateTime.now())){
            throw new AuthorizationFailedException("ATHR-003","Your session is expired. Log in again to access this endpoint");
        }
        //To check if the field is empty
        if(request.getNewPassword().equals("")||request.getOldPassword().equals("")){
            throw new UpdateCustomerException("UCR-003","No field should be empty");
        }

        //To check the strength of password
        if(passwordStrengthService.checkPasswordStrength(request.getNewPassword())==false){
            throw new UpdateCustomerException("UCR-001","Weak Password");
        }

        //To get the old password
        String oldPassword = cryptographyProvider.encrypt(request.getOldPassword(),customer.getSalt());

        if(!(customer.getPassword().equals(oldPassword))){
            throw new UpdateCustomerException("UCR-004","Incorrect old password");
        }

        //if all checks pass, then update the password
        String[] newPassword = cryptographyProvider.encrypt(request.getNewPassword());
        customer.setPassword(newPassword[0]);
        customer.setSalt(newPassword[1]);

        customerService.updateCustomer(customer);

        UpdatePasswordResponse response = new UpdatePasswordResponse();
        response.setStatus("PASSWORD SUCCESSFULLY CHANGED");
        response.setId(customer.getUuid());

        return new ResponseEntity<UpdatePasswordResponse>(response,HttpStatus.CREATED);
    }
}
