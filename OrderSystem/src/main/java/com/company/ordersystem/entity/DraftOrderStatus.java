package com.company.ordersystem.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum DraftOrderStatus implements EnumClass<String> {

    NEW_ORDER("NEW_ORDER"),
    PAID("PAID"),
    WAIT_FOR_RESTAURANT("WAIT_FOR_RESTAURANT"),
    RESTAURANT_COOKING("RESTAURANT_COOKING"),
    COURIER_FINDING("COURIER_FINDING"),
    DELIVERING("DELIVERING"),
    DONE("DONE");

    private final String id;

    DraftOrderStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static DraftOrderStatus fromId(String id) {
        for (DraftOrderStatus at : DraftOrderStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}