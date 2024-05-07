package com.ayd2.adm.library.system.repository;

import com.ayd2.adm.library.system.model.AdmBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdmBookRepository extends JpaRepository<AdmBook, Long> {
}
