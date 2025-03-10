package com.multitenant.auth.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.multitenant.auth.entity.enums.ProviderName;
import com.multitenant.auth.entity.enums.Role;
import com.multitenant.auth.exception.ResourceNotFoundException;
import com.multitenant.auth.utils.PermissionMapping;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "user_entity",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "organization_id"})}
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id", nullable = false)
    @JsonIgnore
    private OrganizationEntity organization;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ProviderEntity> providers;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        roles.forEach(role -> {
            Set<SimpleGrantedAuthority> permissions = PermissionMapping.getAuthoritiesForRole(role);
            authorities.addAll(permissions);
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        ProviderEntity usernamePasswordProvider = this.providers
                                        .stream()
                                        .filter(provider -> provider.getProviderName().equals(ProviderName.USERNAMEPASSWORD.name()))
                                        .findFirst()
                                        .orElseThrow(() -> new ResourceNotFoundException("Username Password provider not found"));
        return usernamePasswordProvider.getPassword();
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
