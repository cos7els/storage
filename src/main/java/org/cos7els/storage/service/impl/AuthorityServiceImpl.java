package org.cos7els.storage.service.impl;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.exception.CustomException;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.model.Authority;
import org.cos7els.storage.repository.AuthorityRepository;
import org.cos7els.storage.service.AuthorityService;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.cos7els.storage.util.ExceptionMessage.AUTHORITIES_NOT_FOUND;
import static org.cos7els.storage.util.ExceptionMessage.AUTHORITY_NOT_FOUND;
import static org.cos7els.storage.util.ExceptionMessage.DELETE_AUTHORITY_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.INSERT_AUTHORITY_EXCEPTION;

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
        List<Authority> authorities = authorityRepository.findAll();
        if (authorities.isEmpty()) {
            throw new NotFoundException(AUTHORITIES_NOT_FOUND);
        }
        return authorities;
    }

    private Authority selectAuthority(Long id) {
        return authorityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(AUTHORITY_NOT_FOUND));
    }

    private Authority insertAuthority(Authority authority) {
        Authority savedAuthority = authorityRepository.save(authority);
        if (savedAuthority == null) {
            throw new CustomException(INSERT_AUTHORITY_EXCEPTION);
        }
        return savedAuthority;
    }

    public void deleteAuthority(Long id) {
        int result = authorityRepository.deleteAuthorityById(id);
        if (result == 0) {
            throw new CustomException(DELETE_AUTHORITY_EXCEPTION);
        }
    }
}