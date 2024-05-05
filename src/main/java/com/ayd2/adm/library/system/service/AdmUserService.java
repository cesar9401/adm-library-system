package com.ayd2.adm.library.system.service;

import com.ayd2.adm.library.system.exception.LibException;
import com.ayd2.adm.library.system.model.AdmUser;
import com.ayd2.adm.library.system.repository.AdmUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdmUserService {

    private final AdmUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<AdmUser> findById(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setPassword(null);
                    return Optional.of(user);
                })
                .orElse(Optional.empty());
    }

    public List<AdmUser> findAll() {
        return userRepository.findAll();
    }

    public AdmUser create(AdmUser entity) throws LibException {
        var userByEmail = userRepository.findByEmail(entity.getEmail());
        if (userByEmail.isPresent()) throw new LibException("email_already_exists");
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        return userRepository.save(entity);
    }

    public AdmUser update(Long userId, AdmUser entity) throws LibException {
        var userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) throw new LibException("user_not_found")
                .status(HttpStatus.NOT_FOUND);

        if (!entity.getUserId().equals(userId)) throw new LibException("invalid_update");
        var duplicatedEmail = userRepository.findDuplicatedByEmailAndNotId(entity.getEmail(), userId);
        if (duplicatedEmail.isPresent()) throw new LibException("email_already_exists");

        entity.setPassword(userOpt.get().getPassword());
        return userRepository.save(entity);
    }
}
