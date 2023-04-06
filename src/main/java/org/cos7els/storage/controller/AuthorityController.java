package org.cos7els.storage.controller;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.model.Authority;
import org.cos7els.storage.service.AuthorityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorityController {
    private final AuthorityService authorityService;

    @GetMapping("/admin/authorities")
    public ResponseEntity<List<Authority>> getAllAuthorities() {
        return new ResponseEntity<>(
                authorityService.getAllAuthorities(),
                HttpStatus.OK
        );
    }

    @GetMapping("/admin/authority/{id}")
    public ResponseEntity<Authority> getAuthority(
            @PathVariable Long id
    ) {
        return new ResponseEntity<>(
                authorityService.getAuthority(id),
                HttpStatus.OK
        );
    }

    @PostMapping("/admin/authority")
    public ResponseEntity<Authority> createAuthority(
            @RequestBody Authority authority
    ) {
        return new ResponseEntity<>(
                authorityService.saveAuthority(authority),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/admin/authority")
    public ResponseEntity<Authority> updateAuthority(
            @RequestBody Authority authority
    ) {
        return new ResponseEntity<>(
                authorityService.saveAuthority(authority),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/admin/authority/{id}")
    public ResponseEntity<HttpStatus> deleteAuthority(
            @PathVariable Long id
    ) {
        authorityService.deleteAuthority(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}