package org.cos7els.storage.service;

import org.cos7els.storage.model.Authority;

import java.util.List;
import java.util.Optional;

public interface AuthorityService {
    Optional<List<Authority>> getAllAuthorities();

    Optional<Authority> getAuthority(Long id);

    Optional<Authority> saveAuthority(Authority authority);

    Integer deleteAuthority(Long id);
}
