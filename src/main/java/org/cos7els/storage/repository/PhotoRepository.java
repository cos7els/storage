package org.cos7els.storage.repository;

import org.cos7els.storage.model.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    List<Photo> getPhotosByUserIdOrderByCreationDate(Long userId);

    Optional<Photo> getPhotoByIdAndUserId(Long photoId, Long userId);

    boolean existsPhotoByPath(String path);

    boolean existsPhotoByFileName(String fileName);
}