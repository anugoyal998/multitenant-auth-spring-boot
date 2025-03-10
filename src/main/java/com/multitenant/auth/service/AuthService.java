package com.multitenant.auth.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.multitenant.auth.dto.LoginRequestBodyDTO;
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
        // TODO: need to modify this method
        boolean emailExists = signupRequestBodyDTO.getEmail() != null && 
                providerRepository
                .findByEmailAndUser_Organization(signupRequestBodyDTO.getEmail(), organization.get())
                .isPresent();

        // 3. Check if a provider with the same phone + country code exists in this organization
        // TODO: need to modify this method
        boolean phoneExists = signupRequestBodyDTO.getPhone() != null && signupRequestBodyDTO.getCountryCode() != null && 
                providerRepository
                .findByPhoneAndCountryCodeAndUser_Organization(signupRequestBodyDTO.getPhone(), signupRequestBodyDTO.getCountryCode(), organization.get())
                .isPresent();
        
        if(emailExists || phoneExists){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "USER already exists");
        }

        // 4. Create User
        UserEntity user = UserEntity.builder()
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
        
        // 5. Generate Tokens
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new TokensResponseDTO(accessToken, refreshToken);
    }

    public TokensResponseDTO login(LoginRequestBodyDTO input){
        // 1. find organization
        Optional<OrganizationEntity> organization = organizationRepository.findByOrgname(input.getOrgname());
        if(organization.isEmpty()){
            throw new ResourceNotFoundException("Organization " + input.getOrgname() + " not found.");
        }

        // 2. Check if a provider with the same email exists in this organization
        // TODO: need to modify this method
        boolean emailExists = input.getEmail() != null && 
                providerRepository
                .findByEmailAndUser_Organization(input.getEmail(), organization.get())
                .isPresent();

        // 3. Check if a provider with the same phone + country code exists in this organization
        // TODO: need to modify this method
        boolean phoneExists = input.getPhone() != null && input.getCountryCode() != null && 
                providerRepository
                .findByPhoneAndCountryCodeAndUser_Organization(input.getPhone(), input.getCountryCode(), organization.get())
                .isPresent();
        
        if(!emailExists && !phoneExists){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "USER not found");
        }

        // 4. Find provider
        ProviderEntity provider = null;
        if(emailExists){
            Optional<ProviderEntity> emailProvider = providerRepository.findByProviderNameAndEmailAndUser_Organization(input.getProviderName(), input.getEmail(), organization.get());
            if(emailProvider.isPresent()){
                provider = emailProvider.get();
            }
        }else{
            Optional<ProviderEntity> phoneProvider = providerRepository.findByProviderNameAndPhoneAndCountryCodeAndUser_Organization(input.getProviderName(), input.getPhone(), input.getCountryCode(), organization.get());
            if(phoneProvider.isPresent()){
                provider = phoneProvider.get();
            }
        }

        if(provider == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "USER not found");
        }

        if(input.getProviderName() == ProviderName.USERNAMEPASSWORD){
            boolean match = passwordEncoder.matches(input.getPassword(), provider.getPassword());
            if(!match){
                throw new BadCredentialsException("Wrong Credentials");
            }
        }

        UserEntity user = provider.getUser();

        // 5. Generate Tokens
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new TokensResponseDTO(accessToken, refreshToken);
    }

    public TokensResponseDTO refresh(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        // TODO: Session Management
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String accessToken = jwtService.generateAccessToken(user);
        return new TokensResponseDTO(accessToken, refreshToken);
    }
}
