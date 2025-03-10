package com.multitenant.auth.dto;

import com.multitenant.auth.entity.UserEntity;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProviderDTO {
    private String providerName;

    private String firstName;

    private String lastName;

    @Email
    private String email;

    private String phone; // phone number validation 

    private String countryCode; // country code validation
    
    private UserEntity user;
}
