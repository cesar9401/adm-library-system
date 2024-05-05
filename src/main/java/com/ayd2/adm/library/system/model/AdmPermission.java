package com.ayd2.adm.library.system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "adm_permission")
@Setter
@Getter
public class AdmPermission {

    @Id
    @Column(name = "permission_id")
    private Long permissionId;

    @JsonBackReference
    @JoinColumn(name = "parent_permission_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AdmPermission parentPermission;

    @Column(name = "name")
    private String name;

    @Column(name = "href")
    private String  href;
}
