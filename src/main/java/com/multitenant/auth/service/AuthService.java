package com.multitenant.auth.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.multitenant.auth.dto.SignupRequestBodyDTO;
import com.multitenant.auth.dto.TokensResponseDTO;
import com.multitenant.auth.entity.OrganizationEntity;
import com.multitenant.auth.entity.ProviderEntity;
import com.multitenant.auth.entity.UserEntity;
import com.multitenant.auth.entity.enums.ProviderName;
import com.multitenant.auth.entity.enums.Role;
import com.multitenant.auth.exception.ResourceNotFoundException;
import com.multitenant.auth.repository.OrganizationRepository;
import com.multitenant.auth.repository.ProviderRepository;
import com.multitenant.auth.repository.UserRepository;
import com.multitenant.auth.utils.UsernameUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final ProviderRepository providerRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public TokensResponseDTO signup(SignupRequestBodyDTO signupRequestBodyDTO){
        // 1. find organization
        Optional<OrganizationEntity> organization = organizationRepository.findByOrgname(signupRequestBodyDTO.getOrgname());
        if(organization.isEmpty()){
            throw new ResourceNotFoundException("Organization " + signupRequestBodyDTO.getOrgname() + " not found.");
        }

        // 2. Check if a provider with the same email exists in this organization
        boolean emailExists = signupRequestBodyDTO.getEmail() != null && 
                providerRepository
                .findByEmailAndUser_Organization(signupRequestBodyDTO.getEmail(), organization.get())
                .isPresent();

        // 3. Check if a provider with the same phone + country code exists in this organization
        boolean phoneExists = signupRequestBodyDTO.getPhone() != null && signupRequestBodyDTO.getCountryCode() != null && 
                providerRepository
                .findByEmailAndUser_Organization(signupRequestBodyDTO.getEmail(), organization.get())
                .isPresent();

        UserEntity user = null;
        if(!emailExists && !phoneExists){
            // 4. Create User
            user = UserEntity.builder()
                                    .username(UsernameUtils.generate(signupRequestBodyDTO))
                                    .organization(organization.get())
                                    .roles(Set.of(Role.USER))
                                    .build();
            ProviderEntity provider = ProviderEntity.builder()
                                    .providerName(signupRequestBodyDTO.getProviderName())
                                    .firstName(signupRequestBodyDTO.getFirstName())
                                    .lastName(signupRequestBodyDTO.getLastName())
                                    .email(signupRequestBodyDTO.getEmail())
                                    .phone(signupRequestBodyDTO.getPhone())
                                    .countryCode(signupRequestBodyDTO.getCountryCode())
                                    .build();

            if(signupRequestBodyDTO.getProviderName() == ProviderName.USERNAMEPASSWORD){
                provider.setPassword(passwordEncoder.encode(signupRequestBodyDTO.getPassword()));
            }
            provider.setUser(user);
            user.setProviders(Set.of(provider));
            userRepository.save(user);
        }else {
            // 4. Login User
            if(signupRequestBodyDTO.getEmail() != null){
                user = userRepository.findByEmail(signupRequestBodyDTO.getEmail());
            }else{
                user = userRepository.findBy
            }
        }
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new TokensResponseDTO(accessToken, refreshToken);
    }

}
