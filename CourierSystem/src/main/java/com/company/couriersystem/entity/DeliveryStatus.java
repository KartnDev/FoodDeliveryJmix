package com.company.couriersystem.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum DeliveryStatus implements EnumClass<String> {

    NEW_REQUEST("NEW_REQUEST"),
    ON_THE_WAY("ON_THE_WAY"),
    DONE("DONE");

    private final String id;

    DeliveryStatus(String id) {
        this.id = id;
    }

    @Nullable
    public static DeliveryStatus fromId(String id) {
        for (DeliveryStatus at : DeliveryStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }

    @Override
    public String getId() {
        return id;
    }
}