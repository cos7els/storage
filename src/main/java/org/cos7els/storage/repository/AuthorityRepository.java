package org.cos7els.storage.repository;

import org.cos7els.storage.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    @Transactional
    Integer deleteAuthorityById(Long id);
}
