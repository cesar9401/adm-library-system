package com.ayd2.adm.library.system.controller;

import com.ayd2.adm.library.system.dto.CollectionPage;
import com.ayd2.adm.library.system.exception.LibException;
import com.ayd2.adm.library.system.model.AdmUser;
import com.ayd2.adm.library.system.service.AdmUserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
public class AdmUserController {

    private final AdmUserService userService;

    public AdmUserController(AdmUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public CollectionPage<List<AdmUser>, Long> findAll(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdmUser> findById(@PathVariable("id") Long userId) {
        return userService.findById(userId)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<AdmUser> create(@RequestBody AdmUser user) throws LibException {
        return new ResponseEntity<>(userService.create(user), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdmUser> update(@PathVariable("id") Long userId, @RequestBody AdmUser entity) throws LibException {
        var updatedUser = userService.update(userId, entity);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
