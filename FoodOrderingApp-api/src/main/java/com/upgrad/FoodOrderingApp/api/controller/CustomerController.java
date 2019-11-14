package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.PasswordCryptographyProvider;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.security.krb5.internal.PAForUserEnc;

import java.util.UUID;

@RestController
@RequestMapping("/")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

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

        if(     //Length check
                !(password.length()>8) ||
                //Special Character check
                !password.matches("(?=.*[~!@#$%^&*()_-]).*") ||
                //Uppercase check
                !password.matches("(?=.*[A-Z]).*") ||
                //Lowercase check
                !password.matches("(?=.*[0-9]).*")
        ){
            throw new SignUpRestrictedException("SGR-004","Weak Password!");
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

}
