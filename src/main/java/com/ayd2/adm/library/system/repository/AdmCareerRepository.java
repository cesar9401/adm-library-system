package com.ayd2.adm.library.system.repository;

import com.ayd2.adm.library.system.model.AdmCareer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdmCareerRepository extends JpaRepository<AdmCareer, Long> {

    Optional<AdmCareer> findByCode(String code);

    Optional<AdmCareer> findByCodeAndCareerIdNot(String code, Long careerId);
}
