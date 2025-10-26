package com.datagear.DataGearInterview.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing transaction types
 */
public enum TransactionType {
    CREDIT("Credit"),
    DEBIT("Debit");

    private final String stringValue;

    TransactionType(String stringValue) {
        this.stringValue = stringValue;
    }

    /**
     * Get the string representation for API
     * @return "Credit" or "Debit"
     */
    @JsonValue
    public String getStringValue() {
        return stringValue;
    }

    /**
     * Convert from string value to TransactionType
     * @param stringValue "Credit" or "Debit" (case insensitive)
     * @return corresponding TransactionType
     */
    @JsonCreator
    public static TransactionType fromString(String stringValue) {
        if (stringValue == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }
        
        String normalizedValue = stringValue.trim().toLowerCase();
        switch (normalizedValue) {
            case "credit":
                return CREDIT;
            case "debit":
                return DEBIT;
            default:
                throw new IllegalArgumentException("Invalid transaction type: " + stringValue + ". Must be 'Credit' or 'Debit'");
        }
    }
}
