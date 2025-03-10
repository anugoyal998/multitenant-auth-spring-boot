package com.multitenant.auth.service;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.multitenant.auth.dto.OrganizationDTO;
import com.multitenant.auth.entity.OrganizationEntity;
import com.multitenant.auth.repository.OrganizationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    
    private final OrganizationRepository organizationRepository;
    private final ModelMapper modelMapper;

    public OrganizationDTO createOrganization(OrganizationDTO input) {
        try {
            if(organizationRepository.existsByOrgname(input.getOrgname())){
                throw new IllegalArgumentException("Organization '" + input.getOrgname() + "' already exists.");
            }
            OrganizationEntity organization = modelMapper.map(input, OrganizationEntity.class);
            organizationRepository.save(organization);
            return modelMapper.map(organization, OrganizationDTO.class);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Organization name must be unique. Organization '" + input.getOrgname() + "' already exists.");
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while creating the organization.", e);
        }
    }

}
