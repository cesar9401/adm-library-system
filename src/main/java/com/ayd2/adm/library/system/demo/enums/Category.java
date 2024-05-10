package com.ayd2.adm.library.system.demo.enums;

public enum Category {

    // TODO: remove when a valid category exists
    EMPTY(0L);

    public final Long internalId;

    Category(Long internalId) {
        this.internalId = internalId;
    }
}
