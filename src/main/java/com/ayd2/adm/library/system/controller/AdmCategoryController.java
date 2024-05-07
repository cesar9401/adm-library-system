package com.ayd2.adm.library.system.controller;

import com.ayd2.adm.library.system.model.AdmCategory;
import com.ayd2.adm.library.system.service.AdmCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("categories")
public class AdmCategoryController {

    private final AdmCategoryService categoryService;

    public AdmCategoryController(AdmCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{internalId}")
    public ResponseEntity<AdmCategory> findByInternalId(@PathVariable("internalId") Long internalId) {
        var category = categoryService.findByInternalId(internalId);
        return category == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping("/by-parent/{parentInternalId}")
    public ResponseEntity<List<AdmCategory>> findByParentInternalId(@PathVariable("parentInternalId") Long parentInternalId) {
        return new ResponseEntity<>(categoryService.findByParentInternalId(parentInternalId), HttpStatus.OK);
    }
}
