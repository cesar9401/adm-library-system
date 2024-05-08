package com.ayd2.adm.library.system.service;

import com.ayd2.adm.library.system.model.AdmBook;
import com.ayd2.adm.library.system.repository.AdmBookRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdmBookService {

    private final AdmBookRepository bookRepository;

    public AdmBookService(AdmBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Optional<AdmBook> findById(Long bookId) {
        return bookRepository.findById(bookId);
    }
}
