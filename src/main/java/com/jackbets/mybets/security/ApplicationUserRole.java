package com.jackbets.mybets.security;

import java.util.Set;

import com.google.common.collect.Sets;

import lombok.Getter;

public enum ApplicationUserRole {
    ADMIN(Sets.newHashSet(ApplicationUserPermission.BET_WRITE));

    private final @Getter Set<ApplicationUserPermission> permissions;

    private ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }
    
}
