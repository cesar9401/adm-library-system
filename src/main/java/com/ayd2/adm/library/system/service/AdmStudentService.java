package com.ayd2.adm.library.system.service;

import com.ayd2.adm.library.system.exception.LibException;
import com.ayd2.adm.library.system.model.AdmStudent;
import com.ayd2.adm.library.system.model.AdmUserRole;
import com.ayd2.adm.library.system.repository.AdmStudentRepository;
import com.ayd2.adm.library.system.util.enums.RoleEnum;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AdmStudentService {

    private final AdmStudentRepository studentRepository;
    private final AdmRoleService roleService;

    public AdmStudentService(
            AdmStudentRepository studentRepository,
            AdmRoleService roleService
    ) {
        this.studentRepository = studentRepository;
        this.roleService = roleService;
    }

    public Optional<AdmStudent> findById(Long studentId) {
        return studentRepository.findById(studentId);
    }

    public List<AdmStudent> findAll() {
        return studentRepository.findAll();
    }

    public AdmStudent create(AdmStudent entity) throws LibException {
        var studentByCarnet = studentRepository.findByCarnet(entity.getCarnet());
        if (studentByCarnet.isPresent()) throw new LibException("carnet_already_exists");

        // find student role
        var roleStudent = roleService.findByRoleId(RoleEnum.STUDENT.roleId);
        var user = entity.getUser();

        // create role
        var userRole = new AdmUserRole();
        userRole.setRole(roleStudent);
        userRole.setUser(user);
        user.setUserRoles(Set.of(userRole));
        return studentRepository.save(entity);
    }

    public AdmStudent update(Long studentId, AdmStudent entity) throws LibException {
        var studentDb = studentRepository.findById(studentId);
        if (studentDb.isEmpty()) throw new LibException("student_not_found").status(HttpStatus.NOT_FOUND);
        if (!studentDb.get().getStudentId().equals(entity.getStudentId())) throw new LibException("invalid_update");
        var studentByCarnet = studentRepository.findByCarnetAndStudentIdNot(entity.getCarnet(), studentId);
        if (studentByCarnet.isPresent()) throw new LibException("carnet_already_exists");

        var student = studentDb.get();
        var user = student.getUser();

        entity.getUser().setUserRoles(user.getUserRoles());
        entity.getUser().setPassword(user.getPassword());
        return studentRepository.save(entity);
    }
}
