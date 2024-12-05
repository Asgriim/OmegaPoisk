package org.omega.authservice.service;


import lombok.RequiredArgsConstructor;
import org.omega.authservice.entity.RoleEntity;
import org.omega.authservice.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    RoleEntity getByRoleId(Long roleId) {
        return roleRepository.findById(roleId).orElse(null);
    }

    RoleEntity getByRoleName(String roleName) {
        return roleRepository.findByName(roleName).orElse(null);
    }
}
