package com.upgrad.FoodOrderingApp.api.exceptionHandler;

import com.upgrad.FoodOrderingApp.api.model.ErrorResponse;
import com.upgrad.FoodOrderingApp.api.model.SaveAddressResponse;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

    //TODO: Check all the return codes
    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signupRestricted(SignUpRestrictedException ex, WebRequest req){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> loginException(AuthorizationFailedException ex,WebRequest req){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()),HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(SaveAddressException.class)
    public ResponseEntity<ErrorResponse> addressException(SaveAddressException ex, WebRequest req){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UpdateCustomerException.class)
    public ResponseEntity<ErrorResponse> updatePasswordException(UpdateCustomerException ex, WebRequest req){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ErrorResponse> restaurantNameException(UpdateCustomerException ex, WebRequest req){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()),HttpStatus.UNAUTHORIZED);
    }
}
