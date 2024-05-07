package com.ayd2.adm.library.system.controller;

import com.ayd2.adm.library.system.dto.CollectionPage;
import com.ayd2.adm.library.system.model.AdmRole;
import com.ayd2.adm.library.system.service.AdmRoleService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("roles")
public class AdmRoleController {

    private final AdmRoleService roleService;

    public AdmRoleController(AdmRoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public CollectionPage<List<AdmRole>, Long> findAll(Pageable pageable) {
        return roleService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdmRole> findById(@PathVariable("id") Long roleId) {
        return roleService.findById(roleId)
                .map(role -> new ResponseEntity<>(role, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
