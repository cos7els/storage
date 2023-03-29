package org.cos7els.storage.repository;

import org.cos7els.storage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByUsername(String username);

    @Transactional
    Integer deleteUserById(Long id);

    boolean existsUserByUsername(String username);
}