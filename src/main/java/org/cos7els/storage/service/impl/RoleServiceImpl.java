package org.cos7els.storage.service.impl;

import org.cos7els.storage.model.Role;
import org.cos7els.storage.repository.RoleRepository;
import org.cos7els.storage.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<Role> getRole(Long id) {
        return roleRepository.findById(id);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    public void deleteAllRoles() {
        roleRepository.deleteAll();
    }

    public boolean isRoleExists(Long id) {
        return roleRepository.existsById(id);
    }
}
