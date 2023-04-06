package org.cos7els.storage.repository;

import liquibase.pro.packaged.Q;
import org.cos7els.storage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User getUserByUsername(String username);

    boolean existsUserByUsername(String username);

    int deleteUserById(Long id);

    @Query(value = "DELETE FROM subscriptions WHERE id = :id;" +
            "DELETE FROM users_authorities WHERE user_id = :id;" +
            "DELETE FROM users WHERE id = :id;",
            nativeQuery = true)
    int deleteUser(Long id);
}