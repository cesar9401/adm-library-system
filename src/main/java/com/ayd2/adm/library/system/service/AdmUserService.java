package com.ayd2.adm.library.system.service;

import com.ayd2.adm.library.system.dto.CollectionPage;
import com.ayd2.adm.library.system.exception.LibException;
import com.ayd2.adm.library.system.model.AdmUser;
import com.ayd2.adm.library.system.model.AdmUserRole;
import com.ayd2.adm.library.system.repository.AdmUserRepository;
import com.ayd2.adm.library.system.util.enums.RoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class AdmUserService {

    private final AdmUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdmRoleService roleService;

    public AdmUserService(
            AdmUserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AdmRoleService roleService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public Optional<AdmUser> findById(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setPassword(null);
                    return Optional.of(user);
                })
                .orElse(Optional.empty());
    }

    public CollectionPage<List<AdmUser>, Long> findAll(Pageable pageable) {
        var users = userRepository.findAll(pageable);
        return CollectionPage.of(users.stream().toList(), users.getTotalElements());
    }

    public AdmUser create(AdmUser entity) throws LibException {
        var userByEmail = userRepository.findByEmail(entity.getEmail());
        if (userByEmail.isPresent()) throw new LibException("email_already_exists");
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));

        var librarianRole = roleService.findByRoleId(RoleEnum.LIBRARIAN.roleId);
        var userRole = new AdmUserRole();
        userRole.setUser(entity);
        userRole.setRole(librarianRole);

        entity.setUserRoles(Set.of(userRole));
        return userRepository.save(entity);
    }

    public AdmUser update(Long userId, AdmUser entity) throws LibException {
        var userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) throw new LibException("user_not_found")
                .status(HttpStatus.NOT_FOUND);

        if (!entity.getUserId().equals(userId)) throw new LibException("invalid_update");
        var duplicatedEmail = userRepository.findDuplicatedByEmailAndNotId(entity.getEmail(), userId);
        if (duplicatedEmail.isPresent()) throw new LibException("email_already_exists");

        var user = userOpt.get();
        entity.setPassword(user.getPassword());
        entity.setUserRoles(user.getUserRoles());// set default roles
        return userRepository.save(entity);
    }
}
