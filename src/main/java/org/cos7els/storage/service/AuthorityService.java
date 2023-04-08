package org.cos7els.storage.service;

import org.cos7els.storage.model.domain.Authority;

import java.util.List;

public interface AuthorityService {
    List<Authority> getAllAuthorities();

    Authority getAuthority(Long id);

    Authority saveAuthority(Authority authority);

    void deleteAuthority(Long id);
}
