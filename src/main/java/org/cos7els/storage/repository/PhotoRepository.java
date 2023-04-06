package org.cos7els.storage.repository;

import org.cos7els.storage.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    List<Photo> findPhotosByUserIdOrderByCreationDate(Long id);

    Optional<Photo> findPhotoByIdAndUserId(Long photoId, Long userId);

    int deletePhotosByUserId(Long userId);

    int removePhotosByUserId(Long userId);
}
