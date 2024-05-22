package com.ayd2.adm.library.system.service;

import com.ayd2.adm.library.system.dto.CollectionPage;
import com.ayd2.adm.library.system.exception.LibException;
import com.ayd2.adm.library.system.model.AdmStudent;
import com.ayd2.adm.library.system.repository.AdmStudentRepository;
import com.ayd2.adm.library.system.util.enums.RoleEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdmStudentService {

    private final AdmStudentRepository studentRepository;
    private final AdmUserService userService;

    public AdmStudentService(
            AdmStudentRepository studentRepository,
            AdmUserService userService
    ) {
        this.studentRepository = studentRepository;
        this.userService = userService;
    }

    public Optional<AdmStudent> findById(Long studentId) {
        return studentRepository.findById(studentId);
    }

    public CollectionPage<List<AdmStudent>, Long> findAll(Pageable pageable) {
        var students = studentRepository.findAll(pageable);
        return CollectionPage.of(students.stream().toList(), students.getTotalElements());
    }

    @Transactional
    public AdmStudent create(AdmStudent entity) throws LibException {
        var studentByCarnet = studentRepository.findByCarnet(entity.getCarnet());
        if (studentByCarnet.isPresent()) throw new LibException("carnet_already_exists");

        var newUser = userService.create(entity.getUser(), RoleEnum.STUDENT.roleId);
        entity.setUser(newUser);
        return studentRepository.save(entity);
    }

    @Transactional
    public AdmStudent update(Long studentId, AdmStudent entity) throws LibException {
        var studentDb = studentRepository.findById(studentId);
        if (studentDb.isEmpty()) throw new LibException("student_not_found").status(HttpStatus.NOT_FOUND);
        if (!studentDb.get().getStudentId().equals(entity.getStudentId())) throw new LibException("invalid_update");
        var studentByCarnet = studentRepository.findByCarnetAndStudentIdNot(entity.getCarnet(), studentId);
        if (studentByCarnet.isPresent()) throw new LibException("carnet_already_exists");

        var user = entity.getUser();
        var updatedUser = userService.update(user.getUserId(), user);
        entity.setUser(updatedUser);
        return studentRepository.save(entity);
    }
}
