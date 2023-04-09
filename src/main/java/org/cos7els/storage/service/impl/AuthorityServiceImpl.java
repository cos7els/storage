package org.cos7els.storage.service.impl;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.exception.InternalException;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.model.domain.Authority;
import org.cos7els.storage.repository.AuthorityRepository;
import org.cos7els.storage.service.AuthorityService;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.cos7els.storage.util.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {
    private final AuthorityRepository authorityRepository;

    public List<Authority> getAllAuthorities() {
        return selectAllAuthorities();
    }

    public Authority getAuthority(Long id) {
        return selectAuthority(id);
    }

    public Authority saveAuthority(Authority authority) {
        return insertAuthority(authority);
    }

    private List<Authority> selectAllAuthorities() {
        return authorityRepository.findAll();
    }

    private Authority selectAuthority(Long id) {
        return authorityRepository.findById(id).orElseThrow(() -> new NotFoundException(AUTHORITY_NOT_FOUND));
    }

    private Authority insertAuthority(Authority authority) {
        Authority savedAuthority = authorityRepository.save(authority);
        if (savedAuthority == null) {
            throw new InternalException(INSERT_AUTHORITY_EXCEPTION);
        }
        return savedAuthority;
    }

    public void deleteAuthority(Long id) {
        int result = authorityRepository.deleteAuthorityById(id);
        if (result == 0) {
            throw new InternalException(DELETE_AUTHORITY_EXCEPTION);
        }
    }
}