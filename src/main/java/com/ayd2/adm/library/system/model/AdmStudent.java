package com.ayd2.adm.library.system.model;

import com.ayd2.adm.library.system.util.LibConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "adm_student")
@Getter
@Setter
public class AdmStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studentIdGenerator")
    @SequenceGenerator(name = "studentIdGenerator", sequenceName = "SEQ_ADM_STUDENT", initialValue = 15000, allocationSize = 1)
    @Column(name = "student_id")
    private Long studentId;

    @JoinColumn(name = "career_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AdmCareer career;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AdmUser user;

    @Column(name = "carnet")
    private String carnet;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = LibConstant.DATE_FORMAT)
    @Column(name = "birthday")
    private LocalDate birthday;
}
