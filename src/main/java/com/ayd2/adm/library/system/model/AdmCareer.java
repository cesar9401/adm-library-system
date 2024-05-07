package com.ayd2.adm.library.system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "adm_career")
@Getter
@Setter
public class AdmCareer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "careerIdGenerator")
    @SequenceGenerator(name = "careerIdGenerator", sequenceName = "SEQ_ADM_CAREER", initialValue = 15000, allocationSize = 1)
    @Column(name = "career_id")
    private Long careerId;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;
}
