package com.project.security.service;

import com.project.security.entity.Roles;

import java.util.List;
import java.util.Set;

public interface RoleRightService {
    Set<Roles> getRolesByIds(List<Integer> roles);
}
