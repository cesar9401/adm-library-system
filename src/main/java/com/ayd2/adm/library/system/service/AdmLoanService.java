package com.ayd2.adm.library.system.service;

import com.ayd2.adm.library.system.dto.CollectionPage;
import com.ayd2.adm.library.system.model.AdmLoan;
import com.ayd2.adm.library.system.repository.AdmLoanRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdmLoanService {

    private final AdmLoanRepository loanRepository;

    public AdmLoanService(AdmLoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public Optional<AdmLoan> findById(Long loanId) {
        return loanRepository.findById(loanId);
    }

    public CollectionPage<List<AdmLoan>, Long> findAll(Pageable pageable) {
        var loans = loanRepository.findAll(pageable);
        return CollectionPage.of(loans.stream().toList(), loans.getTotalElements());
    }
}
