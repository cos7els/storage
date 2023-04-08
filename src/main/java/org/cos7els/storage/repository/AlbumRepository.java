package org.cos7els.storage.repository;

import org.cos7els.storage.model.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findAlbumsByUserId(Long userId);

    Optional<Album> findAlbumByIdAndUserId(Long albumId, Long userId);

    @Transactional
    int deleteAlbumByIdAndUserId(Long albumId, Long userId);

    @Transactional
    int deleteAlbumsByUserId(Long userId);
}