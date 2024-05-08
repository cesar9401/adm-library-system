package com.ayd2.adm.library.system.repository;

import com.ayd2.adm.library.system.model.AdmBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdmBookRepository extends JpaRepository<AdmBook, Long> {

    Optional<AdmBook> findByIsbn(String isbn);

    Optional<AdmBook> findByIsbnAndBookIdNot(String isbn, Long bookId);
}
