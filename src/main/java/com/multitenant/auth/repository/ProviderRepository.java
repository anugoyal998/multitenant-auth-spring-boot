package com.multitenant.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.multitenant.auth.entity.OrganizationEntity;
import com.multitenant.auth.entity.ProviderEntity;
import com.multitenant.auth.entity.enums.ProviderName;

@Repository
public interface ProviderRepository extends JpaRepository<ProviderEntity, Long> {
    Optional<ProviderEntity> findByEmailAndUser_Organization(String email, OrganizationEntity organization);
    Optional<ProviderEntity> findByPhoneAndCountryCodeAndUser_Organization(String phone, String countryCode, OrganizationEntity organization);
    Optional<ProviderEntity> findByProviderNameAndEmailAndUser_Organization(ProviderName providerName, String email,
            OrganizationEntity organizationEntity);
    Optional<ProviderEntity> findByProviderNameAndPhoneAndCountryCodeAndUser_Organization(ProviderName providerName,
            String phone, String countryCode, OrganizationEntity organizationEntity);
}
