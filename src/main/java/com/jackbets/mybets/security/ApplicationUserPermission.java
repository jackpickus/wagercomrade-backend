package com.jackbets.mybets.security;

import lombok.Getter;

public enum ApplicationUserPermission {
    BET_WRITE("bet:write");

    private final @Getter String permission;

    private ApplicationUserPermission(String permission) {
        this.permission = permission;
    }


}
