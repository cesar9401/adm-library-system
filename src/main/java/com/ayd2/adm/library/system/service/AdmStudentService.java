package com.ayd2.adm.library.system.service;

import com.ayd2.adm.library.system.exception.LibException;
import com.ayd2.adm.library.system.model.AdmStudent;
import com.ayd2.adm.library.system.repository.AdmStudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdmStudentService {

    private final AdmStudentRepository studentRepository;

    public AdmStudentService(AdmStudentRepository studentRepository) {
        this.studentRepository = studentRepository;
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
        return studentRepository.save(entity);
    }

    public AdmStudent update(Long studentId, AdmStudent entity) throws LibException {
        var studentDb = studentRepository.findById(studentId);
        if (studentDb.isEmpty()) throw new LibException("student_not_found").status(HttpStatus.NOT_FOUND);
        if (!studentDb.get().getStudentId().equals(entity.getStudentId())) throw new LibException("invalid_update");
        var studentByCarnet = studentRepository.findByCarnetAndStudentIdNot(entity.getCarnet(), studentId);
        if (studentByCarnet.isPresent()) throw new LibException("carnet_already_exists");
        return studentRepository.save(entity);
    }
}
