package com.multitenant.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multitenant.auth.dto.OrganizationDTO;
import com.multitenant.auth.service.OrganizationService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/organization")
@RequiredArgsConstructor
public class OrganizationController {
    
    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<String> createOrganization(@RequestBody OrganizationDTO input) {
        organizationService.createOrganization(input);
        return ResponseEntity.ok("Organization Created Successfully");
    }
    

}
