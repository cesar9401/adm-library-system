package com.ayd2.adm.library.system.model;

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
@Table(name = "adm_role_permission")
@Getter
@Setter
public class AdmRolePermission {

    @Id
    @Column(name = "role_permission_id")
    private Long rolePermissionId;

    @JoinColumn(name = "role_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AdmRole role;

    @JoinColumn(name = "permission_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AdmPermission permission;

    @Column(name = "read_permission")
    private Boolean read;

    @Column(name = "create_permission")
    private Boolean create;

    @Column(name = "update_permission")
    private Boolean update;

    @Column(name = "delete_permission")
    private Boolean delete;

    @Column(name = "export_permission")
    private Boolean export;
}
