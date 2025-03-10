package com.multitenant.auth.utils;

import com.multitenant.auth.dto.SignupRequestBodyDTO;

public class UsernameUtils {
    public static String generate(SignupRequestBodyDTO input){
        // TODO: implement this method
        return input.getEmail();
    }
}
