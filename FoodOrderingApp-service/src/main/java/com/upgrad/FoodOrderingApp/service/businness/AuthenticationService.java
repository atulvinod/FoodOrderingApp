package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AuthTokenDao;
import com.upgrad.FoodOrderingApp.service.dao.UserDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class AuthenticationService {

    @Autowired
    private AuthTokenDao authDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthTokenEntity authenticate(final String contactNumber,final String password) throws AuthorizationFailedException {
        CustomerEntity customer = userDao.getCustomerViaContact(contactNumber);
        if(customer==null){
            throw new AuthorizationFailedException("ATH-001","This contact number has not been registered!");
        }
        String encryptedPassword = cryptographyProvider.encrypt(password,customer.getSalt());

        if(encryptedPassword.equals(customer.getPassword())){
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);

            ZonedDateTime now = ZonedDateTime.now();
            ZonedDateTime expires = now.plusHours(8);

            CustomerAuthTokenEntity authToken = new CustomerAuthTokenEntity();

            authToken.setAccessToken(jwtTokenProvider.generateToken(customer.getUuid(),now,expires));
            authToken.setLoginAt(now);
            authToken.setExpiresAt(expires);
            authToken.setUuid(UUID.randomUUID().toString());
            authToken.setCustomerId(customer.getId());


            authDao.createAuthToken(authToken);
            return authToken;

        }else{
            throw new AuthorizationFailedException("ATH-002","Invalid Credentials");
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthTokenEntity getToken(final String authToken) throws AuthorizationFailedException {
        CustomerAuthTokenEntity token = authDao.checkToken(authToken);
        return token;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateToken(CustomerAuthTokenEntity token){
        authDao.updateToken(token);

    }
}
