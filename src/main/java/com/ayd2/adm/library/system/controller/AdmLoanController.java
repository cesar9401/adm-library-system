package com.ayd2.adm.library.system.controller;

import com.ayd2.adm.library.system.model.AdmLoan;
import com.ayd2.adm.library.system.service.AdmLoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("loans")
public class AdmLoanController {

    private final AdmLoanService loanService;

    public AdmLoanController(AdmLoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping
    public ResponseEntity<List<AdmLoan>> findAll() {
        return ResponseEntity.ok(loanService.findAll());
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<AdmLoan> findById(@PathVariable("loanId") Long loanId) {
        return loanService.findById(loanId)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
