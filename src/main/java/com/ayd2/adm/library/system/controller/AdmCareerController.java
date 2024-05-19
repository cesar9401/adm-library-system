package com.ayd2.adm.library.system.controller;

import com.ayd2.adm.library.system.dto.CollectionPage;
import com.ayd2.adm.library.system.exception.LibException;
import com.ayd2.adm.library.system.model.AdmCareer;
import com.ayd2.adm.library.system.service.AdmCareerService;
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
@RequestMapping("careers")
public class AdmCareerController {

    private final AdmCareerService careerService;

    public AdmCareerController(AdmCareerService careerService) {
        this.careerService = careerService;
    }

    @GetMapping
    public CollectionPage<List<AdmCareer>, Long> findAll(Pageable pageable) {
        return careerService.findAll(pageable);
    }

    @GetMapping("/{careerId}")
    public ResponseEntity<AdmCareer> findById(@PathVariable("careerId") Long careerId) {
        return careerService.findById(careerId)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<AdmCareer> create(@RequestBody AdmCareer entity) throws LibException {
        var newEntity = careerService.create(entity);
        return new ResponseEntity<>(newEntity, HttpStatus.CREATED);
    }

    @PutMapping("/{careerId}")
    public ResponseEntity<AdmCareer> update(@PathVariable("careerId") Long careerId, @RequestBody AdmCareer entity) throws LibException {
        var updatedEntity = careerService.update(careerId, entity);
        return ResponseEntity.ok(updatedEntity);
    }
}
