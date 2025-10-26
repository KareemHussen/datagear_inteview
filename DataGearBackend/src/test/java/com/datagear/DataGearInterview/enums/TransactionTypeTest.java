package com.datagear.DataGearInterview.enums;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TransactionType enum
 */
class TransactionTypeTest {

    @Test
    void getStringValue_Credit_ReturnsCredit() {
        // When
        String result = TransactionType.CREDIT.getStringValue();

        // Then
        assertEquals("Credit", result);
    }

    @Test
    void getStringValue_Debit_ReturnsDebit() {
        // When
        String result = TransactionType.DEBIT.getStringValue();

        // Then
        assertEquals("Debit", result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Credit", "credit", "CREDIT", " Credit ", "CREDIT "})
    void fromString_ValidCreditVariations_ReturnsCredit(String input) {
        // When
        TransactionType result = TransactionType.fromString(input);

        // Then
        assertEquals(TransactionType.CREDIT, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Debit", "debit", "DEBIT", " Debit ", "DEBIT "})
    void fromString_ValidDebitVariations_ReturnsDebit(String input) {
        // When
        TransactionType result = TransactionType.fromString(input);

        // Then
        assertEquals(TransactionType.DEBIT, result);
    }

    @Test
    void fromString_Null_ThrowsException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> TransactionType.fromString(null));

        assertEquals("Transaction type cannot be null", exception.getMessage());
    }

    @Test
    void fromString_EmptyString_ThrowsException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> TransactionType.fromString(""));

        assertEquals("Invalid transaction type: . Must be 'Credit' or 'Debit'", exception.getMessage());
    }

    @Test
    void fromString_InvalidType_ThrowsException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> TransactionType.fromString("InvalidType"));

        assertEquals("Invalid transaction type: InvalidType. Must be 'Credit' or 'Debit'", exception.getMessage());
    }

    @Test
    void fromString_WhitespaceOnly_ThrowsException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> TransactionType.fromString("   "));

        assertEquals("Invalid transaction type:    . Must be 'Credit' or 'Debit'", exception.getMessage());
    }
}
