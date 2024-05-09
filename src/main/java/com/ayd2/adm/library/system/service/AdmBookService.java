package com.ayd2.adm.library.system.service;

import com.ayd2.adm.library.system.exception.LibException;
import com.ayd2.adm.library.system.model.AdmBook;
import com.ayd2.adm.library.system.repository.AdmBookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<AdmBook> findAll() {
        return bookRepository.findAll();
    }

    public AdmBook create(AdmBook entity) throws LibException {
        var isbn = entity.getIsbn();
        var bookByIsbn = bookRepository.findByIsbn(isbn);
        if (bookByIsbn.isPresent()) throw new LibException("isbn_already_exists");
        return bookRepository.save(entity);
    }

    public AdmBook update(Long bookId, AdmBook entity) throws LibException {
        var entityDb = bookRepository.findById(bookId);
        if (entityDb.isEmpty()) throw new LibException("book_not_found").status(HttpStatus.NOT_FOUND);
        if (!entityDb.get().getBookId().equals(entity.getBookId())) throw new LibException("invalid_update");

        var bookByIsbn = bookRepository.findByIsbnAndBookIdNot(entity.getIsbn(), bookId);
        if (bookByIsbn.isEmpty()) throw new LibException("isbn_already_exists");
        return bookRepository.save(entity);
    }
}
