package org.cos7els.storage.repository;

import org.cos7els.storage.model.Photo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PhotoRepository extends CrudRepository<Photo, Long> {
    @Query
    Iterable<Photo> findAllByUserIdIsOrderById(@Param("userId") long userId);
}
