package com.ayd2.adm.library.system.service;

import com.ayd2.adm.library.system.dto.CollectionPage;
import com.ayd2.adm.library.system.exception.LibException;
import com.ayd2.adm.library.system.model.AdmCareer;
import com.ayd2.adm.library.system.repository.AdmCareerRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdmCareerService {

    private final AdmCareerRepository careerRepository;

    public AdmCareerService(AdmCareerRepository careerRepository) {
        this.careerRepository = careerRepository;
    }

    public Optional<AdmCareer> findById(Long careerId) {
        return careerRepository.findById(careerId);
    }

    public CollectionPage<List<AdmCareer>, Long> findAll(Pageable pageable) {
        var careers = careerRepository.findAll(pageable);
        return CollectionPage.of(careers.toList(), careers.getTotalElements());
    }

    public AdmCareer create(AdmCareer entity) throws LibException {
        var careerByCode = careerRepository.findByCode(entity.getCode());
        if (careerByCode.isPresent()) throw new LibException("code_already_exists");
        return careerRepository.save(entity);
    }

    public AdmCareer update(Long careerId, AdmCareer entity) throws LibException {
        var entityDb = careerRepository.findById(careerId);
        if (entityDb.isEmpty()) throw new LibException("career_not_found").status(HttpStatus.NOT_FOUND);
        if (!entityDb.get().getCareerId().equals(entity.getCareerId())) throw new LibException("invalid_update");
        var careerByCode = careerRepository.findByCodeAndCareerIdNot(entity.getCode(), careerId);
        if (careerByCode.isPresent()) throw new LibException("code_already_exists");
        return careerRepository.save(entity);
    }
}
