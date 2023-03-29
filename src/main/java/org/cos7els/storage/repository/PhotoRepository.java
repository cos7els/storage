package org.cos7els.storage.repository;

import org.cos7els.storage.model.Photo;
import org.cos7els.storage.model.response.PhotoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Optional<Photo> findPhotoByIdAndUserId(Long photoId, Long userId);

    List<Photo> findPhotosByUserId(Long id);

    Optional<List<Photo>> findPhotosByUserIdOrderById(Long id);

    void deletePhotoByIdAndUserId(Long photoId, Long userId);

    void deletePhotosByUserId(Long userId);
//    @Query(
//            value = "select * from photos as p inner join " +
//            "(select photo_id from albums_photos where album_id = ?1) as x " +
//            "on p.id = x.photo_id", nativeQuery = true
//    )
//    List<Photo> findPhotosByAlbumIdAndUserId(Long albumId, Long userId);
}
