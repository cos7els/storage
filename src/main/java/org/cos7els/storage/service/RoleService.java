package org.cos7els.storage.service;

import org.cos7els.storage.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Role> getRole(Long id);

    List<Role> getAllRoles();

    Role saveRole(Role role);

    void deleteRole(Long id);

    void deleteAllRoles();

    boolean isRoleExists(Long id);
}
