package com.ayd2.adm.library.system.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
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

@Entity
@Table(name = "adm_user_role")
@Getter
@Setter
public class AdmUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userRoleIdGenerator")
    @SequenceGenerator(name = "userRoleIdGenerator", sequenceName = "SEQ_ADM_USER_ROLE", initialValue = 15000, allocationSize = 1)
    @Column(name = "user_role_id")
    private Long userRoleId;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference("userRoles")
    private AdmUser user;

    @JoinColumn(name = "role_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AdmRole role;
}
