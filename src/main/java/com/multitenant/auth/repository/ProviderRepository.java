package com.multitenant.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.multitenant.auth.entity.OrganizationEntity;
import com.multitenant.auth.entity.ProviderEntity;

@Repository
public interface ProviderRepository extends JpaRepository<ProviderEntity, Long> {
    Optional<ProviderEntity> findByEmailAndUser_Organization(String email, OrganizationEntity organization);
    Optional<ProviderEntity> findByPhoneAndCountryCodeAndUser_Organization(String phone, String countryCode, OrganizationEntity organization);
}
