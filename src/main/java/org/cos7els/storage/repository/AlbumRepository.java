package org.cos7els.storage.repository;

import org.cos7els.storage.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {
}
