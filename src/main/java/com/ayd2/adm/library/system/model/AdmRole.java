package com.ayd2.adm.library.system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "adm_role")
@Getter
@Setter
public class AdmRole {

    @Id
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdmRole admRole = (AdmRole) o;

        return roleId.equals(admRole.roleId);
    }

    @Override
    public int hashCode() {
        return roleId.hashCode();
    }
}
