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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestBodyDTO {
    @NotNull
    private String orgname;

    @NotNull
    private ProviderName providerName;

    @Email
    private String email;

    private String phone; // TODO: phone number validation 

    private String countryCode; // TODO: country code validation

    private String password;
}
