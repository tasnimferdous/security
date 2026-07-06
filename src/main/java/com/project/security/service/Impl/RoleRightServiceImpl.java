package com.project.security.service.Impl;

import com.project.security.entity.Roles;
import com.project.security.repository.RoleRepository;
import com.project.security.service.RoleRightService;
import com.tasnim.commonlibrary.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class RoleRightServiceImpl implements RoleRightService {
    private final RoleRepository roleRepository;

    public RoleRightServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Set<Roles> getRolesByIds(List<Integer> roleIds) {
        log.info("Fetching roles by IDs: {}", roleIds);
        Set<Roles> roles = new HashSet<>(roleRepository.findAllRolesById(roleIds));

        if (roles.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Role not found");
        }
        return roles;
    }
}
