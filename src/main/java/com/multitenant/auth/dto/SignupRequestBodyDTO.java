package com.multitenant.auth.dto;

import com.multitenant.auth.entity.enums.ProviderName;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
public class SignupRequestBodyDTO {
    
    @NotNull
    private String orgname;
    
    @NotNull
    private ProviderName providerName;

    @NotNull
    private String firstName;

    private String lastName;

    @Email
    private String email;

    private String phone; // phone number validation 

    private String countryCode; // country code validation

    private String password;
}
