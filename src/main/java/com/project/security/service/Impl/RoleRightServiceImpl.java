package com.project.security.service.Impl;

import com.project.security.entity.Roles;
import com.project.security.repository.RoleRepository;
import com.project.security.service.RoleRightService;
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
        try {
            log.info("Fetch Roles By Ids: {}", roleIds);
            return new HashSet<>(roleRepository.findAllRolesById(roleIds));
        } catch (Exception e) {
            log.error("Exception - ",e);
            throw new RuntimeException("Failed to fetch roles by IDs: " + e.getMessage());
        }
    }
}
