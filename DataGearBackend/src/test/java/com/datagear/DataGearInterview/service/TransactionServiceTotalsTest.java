package com.datagear.DataGearInterview.service;

import com.datagear.DataGearInterview.dto.projection.TransactionTotalsProjection;
import com.datagear.DataGearInterview.mapper.TransactionMapper;
import com.datagear.DataGearInterview.repository.TransactionRepository;
import com.datagear.DataGearInterview.dto.response.TransactionTotalsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTotalsTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void cleanDb() {
        transactionRepository.deleteAll();
    }

    @Test
    void getTransactionTotals_WithTransactions_ReturnsCorrectTotals() {
        // Given
        TransactionTotalsProjection projection = mock(TransactionTotalsProjection.class);
        when(projection.getTotalCreditAmount()).thenReturn(new BigDecimal("350.50"));
        when(projection.getTotalDebitAmount()).thenReturn(new BigDecimal("226.00"));
        when(projection.getTotalCreditCount()).thenReturn(2L);
        when(projection.getTotalDebitCount()).thenReturn(2L);
        when(transactionRepository.getTransactionTotals()).thenReturn(projection);

        // When
        TransactionTotalsResponse result = transactionService.getTransactionTotals();

        // Then
        assertNotNull(result);
        assertEquals(new BigDecimal("350.50"), result.getTotalCreditAmount());
        assertEquals(new BigDecimal("226.00"), result.getTotalDebitAmount());
        assertEquals(2L, result.getTotalCreditCount());
        assertEquals(2L, result.getTotalDebitCount());
        assertEquals(new BigDecimal("124.50"), result.getNetAmount());
        assertEquals(4L, result.getTotalTransactionCount());

        verify(transactionRepository).getTransactionTotals();
    }

    @Test
    void getTransactionTotals_WithNoTransactions_ReturnsZeroTotals() {
        // Given
        TransactionTotalsProjection projection = mock(TransactionTotalsProjection.class);
        when(projection.getTotalCreditAmount()).thenReturn(BigDecimal.ZERO);
        when(projection.getTotalDebitAmount()).thenReturn(BigDecimal.ZERO);
        when(projection.getTotalCreditCount()).thenReturn(0L);
        when(projection.getTotalDebitCount()).thenReturn(0L);
        when(transactionRepository.getTransactionTotals()).thenReturn(projection);

        // When
        TransactionTotalsResponse result = transactionService.getTransactionTotals();

        // Then
        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getTotalCreditAmount());
        assertEquals(BigDecimal.ZERO, result.getTotalDebitAmount());
        assertEquals(0L, result.getTotalCreditCount());
        assertEquals(0L, result.getTotalDebitCount());
        assertEquals(BigDecimal.ZERO, result.getNetAmount());
        assertEquals(0L, result.getTotalTransactionCount());
    }

    @Test
    void getTransactionTotals_WithOnlyCreditTransactions_ReturnsCorrectTotals() {
        // Given
        TransactionTotalsProjection projection = mock(TransactionTotalsProjection.class);
        when(projection.getTotalCreditAmount()).thenReturn(new BigDecimal("350.50"));
        when(projection.getTotalDebitAmount()).thenReturn(BigDecimal.ZERO);
        when(projection.getTotalCreditCount()).thenReturn(2L);
        when(projection.getTotalDebitCount()).thenReturn(0L);
        when(transactionRepository.getTransactionTotals()).thenReturn(projection);

        // When
        TransactionTotalsResponse result = transactionService.getTransactionTotals();

        // Then
        assertNotNull(result);
        assertEquals(new BigDecimal("350.50"), result.getTotalCreditAmount());
        assertEquals(BigDecimal.ZERO, result.getTotalDebitAmount());
        assertEquals(2L, result.getTotalCreditCount());
        assertEquals(0L, result.getTotalDebitCount());
        assertEquals(new BigDecimal("350.50"), result.getNetAmount());
        assertEquals(2L, result.getTotalTransactionCount());
    }

    @Test
    void getTransactionTotals_WithOnlyDebitTransactions_ReturnsCorrectTotals() {
        // Given
        TransactionTotalsProjection projection = mock(TransactionTotalsProjection.class);
        when(projection.getTotalCreditAmount()).thenReturn(BigDecimal.ZERO);
        when(projection.getTotalDebitAmount()).thenReturn(new BigDecimal("226.00"));
        when(projection.getTotalCreditCount()).thenReturn(0L);
        when(projection.getTotalDebitCount()).thenReturn(2L);
        when(transactionRepository.getTransactionTotals()).thenReturn(projection);

        // When
        TransactionTotalsResponse result = transactionService.getTransactionTotals();

        // Then
        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getTotalCreditAmount());
        assertEquals(new BigDecimal("226.00"), result.getTotalDebitAmount());
        assertEquals(0L, result.getTotalCreditCount());
        assertEquals(2L, result.getTotalDebitCount());
        assertEquals(new BigDecimal("-226.00"), result.getNetAmount());
        assertEquals(2L, result.getTotalTransactionCount());
    }
}
