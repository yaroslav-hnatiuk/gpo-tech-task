package com.github.yhnatiuk.gpotechtask.service.dto;

import org.keycloak.representations.idm.RoleRepresentation;

import java.util.List;

public class UserRoleDto {
    private String id;
    private List<RoleRepresentation> roles;

    public UserRoleDto() {
    }

    public UserRoleDto(String id, List<RoleRepresentation> roles) {
        this.id = id;
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<RoleRepresentation> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleRepresentation> roles) {
        this.roles = roles;
    }
}
