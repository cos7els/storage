package org.cos7els.storage.repository;

import org.cos7els.storage.model.Content;
import org.springframework.data.repository.CrudRepository;

public interface ContentRepository extends CrudRepository<Content, Long> {
}