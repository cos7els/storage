package org.cos7els.storage.repository;

import org.cos7els.storage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByUsername(String username);

    User getUserByUsername(String username);

    int deleteUserById(long userId);
}