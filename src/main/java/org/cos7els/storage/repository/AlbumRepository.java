package org.cos7els.storage.repository;

import org.cos7els.storage.model.Album;
import org.springframework.data.repository.CrudRepository;

public interface AlbumRepository extends CrudRepository<Album, Long> {
}
