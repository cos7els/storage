package org.cos7els.storage.repository;

import org.cos7els.storage.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    @Transactional
    int deleteAuthorityById(Long id);
}
