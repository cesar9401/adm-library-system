package com.ayd2.adm.library.system.controller;

import com.ayd2.adm.library.system.dto.CollectionPage;
import com.ayd2.adm.library.system.exception.LibException;
import com.ayd2.adm.library.system.model.AdmBook;
import com.ayd2.adm.library.system.service.AdmBookService;
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
@RequestMapping("books")
public class AdmBookController {

    private final AdmBookService bookService;

    public AdmBookController(AdmBookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public CollectionPage<List<AdmBook>, Long> findAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<AdmBook> findById(@PathVariable("bookId") Long bookId) {
        return bookService.findById(bookId)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<AdmBook> create(@RequestBody AdmBook entity) throws LibException {
        var newEntity = bookService.create(entity);
        return new ResponseEntity<>(newEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<AdmBook> update(@PathVariable("bookId") Long bookId, @RequestBody AdmBook entity) throws LibException {
        var updatedEntity = bookService.update(bookId, entity);
        return ResponseEntity.ok(updatedEntity);
    }
}
