package com.ayd2.adm.library.system.model;

import com.ayd2.adm.library.system.util.LibConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "adm_user")
@Getter
@Setter
public class AdmUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userIdGenerator")
    @SequenceGenerator(name = "userIdGenerator", sequenceName = "SEQ_ADM_USER", initialValue = 15000, allocationSize = 1)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @JsonFormat(pattern = LibConstant.DATE_FORMAT, shape = JsonFormat.Shape.STRING)
    @Column(name = "birthday")
    private LocalDate birthday;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    @JsonManagedReference("userRoles")
    private Set<AdmUserRole> userRoles;
}
