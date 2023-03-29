package org.cos7els.storage.controller;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.exception.CustomException;
import org.cos7els.storage.exception.NotFoundException;
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

import static org.cos7els.storage.util.ExceptionMessage.AUTHORITIES_NOT_FOUND;
import static org.cos7els.storage.util.ExceptionMessage.AUTHORITY_NOT_FOUND;
import static org.cos7els.storage.util.ExceptionMessage.CREATE_AUTHORITY_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.UPDATE_AUTHORITY_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.DELETE_AUTHORITY_EXCEPTION;

@RestController
@RequiredArgsConstructor
public class AuthorityController {
    private final AuthorityService authorityService;

    @GetMapping("/admin/authorities")
    public ResponseEntity<List<Authority>> getAllAuthorities() {
        List<Authority> authorities = authorityService.getAllAuthorities()
                .orElseThrow(() -> new NotFoundException(AUTHORITIES_NOT_FOUND));
        return new ResponseEntity<>(authorities, HttpStatus.OK);
    }

    @GetMapping("/admin/authority/{id}")
    public ResponseEntity<Authority> getAuthority(@PathVariable Long id) {
        Authority authority = authorityService.getAuthority(id)
                .orElseThrow(() -> new NotFoundException(AUTHORITY_NOT_FOUND));
        return new ResponseEntity<>(authority, HttpStatus.OK);
    }

    @PostMapping("/admin/authority")
    public ResponseEntity<Authority> createAuthority(@RequestBody Authority request) {
        Authority authority = authorityService.saveAuthority(request)
                .orElseThrow(() -> new CustomException(CREATE_AUTHORITY_EXCEPTION));
        return new ResponseEntity<>(authority, HttpStatus.CREATED);
    }

    @PutMapping("/admin/authority")
    public ResponseEntity<Authority> updateAuthority(@RequestBody Authority request) {
        Authority authority = authorityService.saveAuthority(request)
                .orElseThrow(() -> new CustomException(UPDATE_AUTHORITY_EXCEPTION));
        return new ResponseEntity<>(authority, HttpStatus.OK);
    }

    @DeleteMapping("/admin/authority/{id}")
    public ResponseEntity<HttpStatus> deleteAuthority(@PathVariable Long id) {
        Integer result = authorityService.deleteAuthority(id);
        if (result == 0) {
            throw new CustomException(DELETE_AUTHORITY_EXCEPTION);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}