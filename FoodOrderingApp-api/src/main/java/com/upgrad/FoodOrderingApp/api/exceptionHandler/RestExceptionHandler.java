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
    if(ex.getCode().equals("SGR-001")){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()), HttpStatus.FORBIDDEN);

    }else {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()), HttpStatus.BAD_REQUEST);

    }
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationException(AuthenticationFailedException ex,WebRequest req){
        String errorCode = ex.getCode();
        if(errorCode.equals("ATH-002")){ //Invalid credentials
            return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()),HttpStatus.FORBIDDEN);

        }else if(errorCode.equals("ATH-001")){ //Contact not found
            return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()),HttpStatus.NOT_FOUND);

        }else if(errorCode.equals("ATH-003")){ //Bad request
            return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()),HttpStatus.BAD_REQUEST);

        }else{
            return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()),HttpStatus.UNAUTHORIZED);

        }
    }

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> loginException(AuthorizationFailedException ex,WebRequest req){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()),HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(SaveAddressException.class)
    public ResponseEntity<ErrorResponse> addressException(SaveAddressException ex, WebRequest req){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UpdateCustomerException.class)
    public ResponseEntity<ErrorResponse> updatePasswordException(UpdateCustomerException ex, WebRequest req){
    if(ex.getCode().equals("UCR-001")||ex.getCode().equals("UCR-004")){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()),HttpStatus.BAD_REQUEST);

    }else{
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()),HttpStatus.UNAUTHORIZED);

    }
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ErrorResponse> restaurantNameException(RestaurantNotFoundException ex, WebRequest req){
    if(ex.getCode().equals("RNF-001")){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()),HttpStatus.NOT_FOUND);

    }else if(ex.getCode().equals("RNF-003")||ex.getCode().equals("RNF-002")){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()),HttpStatus.BAD_REQUEST);

    }else{
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()),HttpStatus.UNAUTHORIZED);

    }
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> categoryException(CategoryNotFoundException ex, WebRequest req){
    if(ex.getCode().equals("RNF-003")){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()),HttpStatus.BAD_REQUEST);

    }else{
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(ex.getCode()).message(ex.getErrorMessage()),HttpStatus.NOT_FOUND);

    }
    }
}
