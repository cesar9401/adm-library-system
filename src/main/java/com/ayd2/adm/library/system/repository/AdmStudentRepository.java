package com.ayd2.adm.library.system.repository;

import com.ayd2.adm.library.system.model.AdmStudent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdmStudentRepository extends JpaRepository<AdmStudent, Long> {
}
