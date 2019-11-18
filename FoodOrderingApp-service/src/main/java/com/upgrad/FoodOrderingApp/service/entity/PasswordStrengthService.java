package com.upgrad.FoodOrderingApp.service.entity;

import org.springframework.stereotype.Service;

@Service
public class PasswordStrengthService {
    public boolean checkPasswordStrength(String password){

        if (password.matches("(?=.*[0-9]).*")
        && password.matches("(?=.*[a-z]).*")
        && password.matches("(?=.*[A-Z]).*")
        && password.matches("(?=.*[~!@#$%^&*()_-]).*")){
            return true;
        }else{
            return false;
        }
    }
}
