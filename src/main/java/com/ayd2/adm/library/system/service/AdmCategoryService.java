package com.ayd2.adm.library.system.service;

import com.ayd2.adm.library.system.model.AdmCategory;
import com.ayd2.adm.library.system.repository.AdmCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdmCategoryService {

    private final AdmCategoryRepository categoryRepository;

    public AdmCategoryService(AdmCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public AdmCategory findByInternalId(Long internalId) {
        return categoryRepository.findByInternalId(internalId);
    }

    public List<AdmCategory> findByParentInternalId(Long parentInternalId) {
        return categoryRepository.findByParentInternalId(parentInternalId);
    }
}
