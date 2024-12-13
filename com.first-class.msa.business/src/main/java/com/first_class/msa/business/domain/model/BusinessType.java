package com.first_class.msa.business.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessType {

    PRODUCER(Label.PRODUCER),  // 생산업체
    RECEIVER(Label.RECEIVER);  // 수령업체

    private final String label;

    public static class Label {
        public static final String PRODUCER = "Producer";
        public static final String RECEIVER = "Receiver";
    }

    public static BusinessType fromLabel(String label) {
        return switch (label) {
            case Label.PRODUCER -> BusinessType.PRODUCER;
            case Label.RECEIVER -> BusinessType.RECEIVER;
            default -> throw new IllegalArgumentException("Invalid Business Type: " + label);
        };
    }
}
