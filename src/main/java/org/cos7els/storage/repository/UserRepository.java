package org.cos7els.storage.repository;

import org.cos7els.storage.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long> {
    @Modifying
    @Query("update User u set u.usedSpace = :usedSpace where u.id = :id")
    void updateUserUsedSpace(@Param("id") long id, @Param("usedSpace") double usedSpace);

    User findUserByUserName(@Param("userName") String userName);
}
