package com.ayd2.adm.library.system.util.enums;

public enum RoleEnum {

    LIBRARIAN(5000L),
    STUDENT(5001L);

    public final Long roleId;

    RoleEnum(Long roleId) {
        this.roleId = roleId;
    }
}
