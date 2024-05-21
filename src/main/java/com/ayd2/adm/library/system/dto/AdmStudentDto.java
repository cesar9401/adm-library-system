package com.ayd2.adm.library.system.dto;

import com.ayd2.adm.library.system.model.AdmCareer;
import com.ayd2.adm.library.system.util.LibConstant;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record AdmStudentDto(
        Long userId,
        Long studentId,
        String carnet,
        String email,
        String fullName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = LibConstant.DATE_FORMAT)
        LocalDate birthday,
        AdmCareer career
) {
}
