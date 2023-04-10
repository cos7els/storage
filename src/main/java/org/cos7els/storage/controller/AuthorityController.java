package org.cos7els.storage.controller;

import org.cos7els.storage.model.domain.Authority;
import org.cos7els.storage.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AuthorityController {
    private final AuthorityService authorityService;

    @Autowired
    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @GetMapping("/admin/authorities")
    public ResponseEntity<List<Authority>> getAuthorities() {
        return new ResponseEntity<>(authorityService.getAllAuthorities(), HttpStatus.OK);
    }

    @GetMapping("/admin/authority/{authorityId}")
    public ResponseEntity<Authority> getAuthority(@PathVariable Long authorityId) {
        return new ResponseEntity<>(authorityService.getAuthority(authorityId), HttpStatus.OK);
    }

    @PostMapping("/admin/authority")
    public ResponseEntity<Authority> createAuthority(@RequestBody Authority authority) {
        return new ResponseEntity<>(authorityService.saveAuthority(authority), HttpStatus.CREATED);
    }

    @PutMapping("/admin/authority")
    public ResponseEntity<Authority> updateAuthority(@RequestBody Authority authority) {
        return new ResponseEntity<>(authorityService.saveAuthority(authority), HttpStatus.OK);
    }

    @DeleteMapping("/admin/authority/{authorityId}")
    public ResponseEntity<HttpStatus> deleteAuthority(@PathVariable Long authorityId) {
        authorityService.deleteAuthority(authorityId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}