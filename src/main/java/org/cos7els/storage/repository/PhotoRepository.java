package org.cos7els.storage.repository;

import org.cos7els.storage.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Optional<Photo> findPhotoByIdAndUserId(Long photoId, Long userId);

    List<Photo> findPhotosByUserId(Long id);

    void deletePhotoByIdAndUserId(Long photoId, Long userId);

    void deletePhotosByUserId(Long userId);
}
