package org.cos7els.storage.service;

import org.cos7els.storage.model.domain.Authority;
import org.cos7els.storage.repository.AuthorityRepository;
import org.cos7els.storage.service.impl.AuthorityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorityServiceTest {
    @Mock
    private AuthorityRepository authorityRepository;
    @InjectMocks
    private AuthorityServiceImpl authorityService;
    private Long id;
    private Authority authority;
    private List<Authority> authorities;

    @BeforeEach
    public void init() {
        id = 1L;
        authority = new Authority();
        authorities = List.of(new Authority());
    }

    @Test
    public void getAllAuthoritiesTest() {
        when(authorityRepository.findAll()).thenReturn(authorities);
        List<Authority> returned = authorityService.getAllAuthorities();
        verify(authorityRepository).findAll();
        assertEquals(authorities, returned);
    }

    @Test
    public void getAuthorityTest() {
        when(authorityRepository.findById(id)).thenReturn(Optional.of(authority));
        Authority returned = authorityService.getAuthority(id);
        verify(authorityRepository).findById(id);
        assertEquals(authority, returned);
    }

    @Test
    public void saveAuthorityTest() {
        when(authorityRepository.save(authority)).thenReturn(authority);
        Authority returned = authorityService.saveAuthority(authority);
        verify(authorityRepository).save(authority);
        assertEquals(authority, returned);
    }

    @Test
    public void deleteAuthorityTest() {
        when(authorityRepository.deleteAuthorityById(id)).thenReturn(id.intValue());
        authorityService.deleteAuthority(id);
        verify(authorityRepository).deleteAuthorityById(id);
    }
}