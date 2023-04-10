package org.cos7els.storage.repository;

import org.cos7els.storage.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);

    User getUserByUsername(String username);

    int deleteUserById(Long userId);

    @Query(value = "SELECT username FROM users WHERE id = :userId", nativeQuery = true)
    String getUsernameById(Long userId);
}