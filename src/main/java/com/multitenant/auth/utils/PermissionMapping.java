package com.multitenant.auth.utils;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.multitenant.auth.entity.enums.Permission;
import com.multitenant.auth.entity.enums.Role;

public class PermissionMapping {
    private static final Map<Role, Set<Permission>> map = Map.of(
        Role.USER, Set.of(),
        Role.ADMIN, Set.of(Permission.USER_VIEW_ALL)
    ); 

    public static Set<SimpleGrantedAuthority> getAuthoritiesForRole(Role role){
        return map.get(role).stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.name()))
                    .collect(Collectors.toSet());
    }
}
