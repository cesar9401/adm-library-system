package com.ayd2.adm.library.system.service;

import com.ayd2.adm.library.system.model.AdmRole;
import com.ayd2.adm.library.system.dto.CollectionPage;
import com.ayd2.adm.library.system.repository.AdmRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class AdmRoleService {

    private final AdmRoleRepository roleRepository;

    public AdmRoleService(AdmRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<AdmRole> findById(Long roleId) {
        return roleRepository.findById(roleId);
    }

    public CollectionPage<List<AdmRole>, Long> findAll(Pageable pageable) {
        log.info("Page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        var roles = roleRepository.findAll(pageable);
        return CollectionPage.of(roles.toList(), roles.getTotalElements());
    }

    public Set<AdmRole> findRolesByUserId(Long userId) {
        return roleRepository.findAllRolesByUserId(userId);
    }
}
