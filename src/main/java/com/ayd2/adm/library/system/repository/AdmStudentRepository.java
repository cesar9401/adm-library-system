package com.ayd2.adm.library.system.repository;

import com.ayd2.adm.library.system.model.AdmStudent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdmStudentRepository extends JpaRepository<AdmStudent, Long> {

    Optional<AdmStudent> findByCarnet(String carnet);

    Optional<AdmStudent> findByCarnetAndStudentIdNot(String carnet, Long userId);
}
