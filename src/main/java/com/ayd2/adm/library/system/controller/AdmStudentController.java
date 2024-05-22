package com.ayd2.adm.library.system.controller;

import com.ayd2.adm.library.system.dto.CollectionPage;
import com.ayd2.adm.library.system.exception.LibException;
import com.ayd2.adm.library.system.model.AdmStudent;
import com.ayd2.adm.library.system.service.AdmStudentService;
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
@RequestMapping("students")
public class AdmStudentController {

    private final AdmStudentService studentService;

    public AdmStudentController(AdmStudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<CollectionPage<List<AdmStudent>, Long>> findAll(Pageable pageable) {
        var collectionPage = studentService.findAll(pageable);
        return new ResponseEntity<>(collectionPage, HttpStatus.OK);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<AdmStudent> findById(@PathVariable("studentId") Long studentId) {
        return studentService.findById(studentId)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<AdmStudent> create(@RequestBody AdmStudent entity) throws LibException {
        var newEntity = studentService.create(entity);
        return new ResponseEntity<>(newEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<AdmStudent> update(@PathVariable("studentId") Long studentId, @RequestBody AdmStudent entity) throws LibException {
        var updatedEntity = studentService.update(studentId, entity);
        return ResponseEntity.ok(updatedEntity);
    }
}
