package org.cos7els.storage.repository;

import org.cos7els.storage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByUsername(String username);
    boolean existsUserByUsername(String username);
//    User getUser(Long id);
}