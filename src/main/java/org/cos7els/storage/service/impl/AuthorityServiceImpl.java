package org.cos7els.storage.service.impl;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.model.Authority;
import org.cos7els.storage.repository.AuthorityRepository;
import org.cos7els.storage.service.AuthorityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {
    private final AuthorityRepository authorityRepository;

    public Optional<List<Authority>> getAllAuthorities() {
        return Optional.of(authorityRepository.findAll());
    }

    public Optional<Authority> getAuthority(Long id) {
        return authorityRepository.findById(id);
    }

    public Optional<Authority> saveAuthority(Authority authority) {
        return Optional.of(authorityRepository.save(authority));
    }

    public Integer deleteAuthority(Long id) {
        return authorityRepository.deleteAuthorityById(id);
    }
}