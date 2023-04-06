package org.cos7els.storage.repository;

import org.cos7els.storage.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

//    List<Photo> findPhotosByUserIdOrderByCreationDate(Long id);

//    Optional<Photo> getPhotoByIdAndUserId(Long photoId, Long userId);

    @Query(value = "SELECT * FROM photos WHERE user_id = :userId",
            nativeQuery = true)
    List<Photo> findPhotosByUserIdOrderByCreationDate(Long userId);

    @Query(value = "SELECT * FROM photos WHERE id = :photoId AND user_id = :userId",
            nativeQuery = true)
    Optional<Photo> getPhotoByIdAndUserId(Long photoId, Long userId);

    @Query(value = "SELECT * FROM photos WHERE id IN :ids",
            nativeQuery = true)
    List<Photo> getPhotosByIds(List<Long> ids);

    @Modifying
    @Query(value = "DELETE FROM albums_photos WHERE photo_id IN :ids ;" +
            "DELETE FROM photos WHERE id IN :ids",
            nativeQuery = true)
    int deletePhotosByIds(List<Long> ids);

    @Modifying
    @Query(value = "DELETE FROM albums_photos WHERE photo_id IN " +
            "(SELECT id FROM photos WHERE user_id = :id);" +
            "DELETE FROM photos WHERE id = :userId",
            nativeQuery = true)
    int deletePhotosByUserId(Long userId);
}