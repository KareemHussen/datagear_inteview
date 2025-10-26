package com.datagear.DataGearInterview.controller;

import com.datagear.DataGearInterview.dto.request.TransactionCreateRequest;
import com.datagear.DataGearInterview.dto.request.TransactionIndexRequest;
import com.datagear.DataGearInterview.dto.request.TransactionSearchRequest;
import com.datagear.DataGearInterview.dto.response.TransactionPageResponse;
import com.datagear.DataGearInterview.dto.response.TransactionResponse;
import com.datagear.DataGearInterview.dto.response.TransactionTotalsResponse;
import com.datagear.DataGearInterview.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for TransactionController
 */
@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private TransactionCreateRequest createRequest;
    private TransactionResponse transactionResponse;
    private TransactionPageResponse pageResponse;

    @BeforeEach
    void setUp() {
        createRequest = TransactionCreateRequest.builder()
            .amount(new BigDecimal("100.50"))
            .type("Credit")
            .note("Test transaction")
            .build();

        transactionResponse = TransactionResponse.builder()
            .id(1L)
            .amount(new BigDecimal("100.50"))
            .type("Credit")
            .note("Test transaction")
            .createdAt(LocalDateTime.now())
            .build();

        List<TransactionResponse> transactions = Arrays.asList(transactionResponse);
        pageResponse = TransactionPageResponse.builder()
            .transactions(transactions)
            .currentPage(0)
            .totalPages(1)
            .totalElements(1)
            .size(10)
            .first(true)
            .last(true)
            .build();
    }

    @Test
    void createTransaction_Success() throws Exception {
        // Given
        when(transactionService.createTransaction(any(TransactionCreateRequest.class)))
            .thenReturn(transactionResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Transaction created successfully"))
                .andExpect(jsonPath("$.body.id").value(1))
                .andExpect(jsonPath("$.body.amount").value(100.50))
                .andExpect(jsonPath("$.body.type").value("Credit"))
                .andExpect(jsonPath("$.body.note").value("Test transaction"));
    }

    @Test
    void createTransaction_WithDebitType_Success() throws Exception {
        // Given
        createRequest.setType("Debit");
        transactionResponse.setType("Debit");

        when(transactionService.createTransaction(any(TransactionCreateRequest.class)))
            .thenReturn(transactionResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Transaction created successfully"))
                .andExpect(jsonPath("$.body.type").value("Debit"));
    }

    @Test
    void createTransaction_InvalidAmount_ValidationError() throws Exception {
        // Given
        createRequest.setAmount(new BigDecimal("-10.00")); // Invalid negative amount

        // When & Then
        mockMvc.perform(post("/api/v1/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid request data"))
                .andExpect(jsonPath("$.errors.amount").value("Amount must be positive"));
    }

    @Test
    void createTransaction_NullAmount_ValidationError() throws Exception {
        // Given
        createRequest.setAmount(null);

        // When & Then
        mockMvc.perform(post("/api/v1/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid request data"))
                .andExpect(jsonPath("$.errors.amount").value("Amount cannot be null"));
    }

    @Test
    void createTransaction_NullType_ValidationError() throws Exception {
        // Given
        createRequest.setType(null);

        // When & Then
        mockMvc.perform(post("/api/v1/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid request data"))
                .andExpect(jsonPath("$.errors.type").value("Transaction type cannot be blank"));
    }

    @Test
    void createTransaction_InvalidType_ValidationError() throws Exception {
        // Given
        createRequest.setType("InvalidType");
        when(transactionService.createTransaction(any(TransactionCreateRequest.class)))
            .thenThrow(new IllegalArgumentException("Invalid transaction type: InvalidType. Must be 'Credit' or 'Debit'"));

        // When & Then
        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid request data"))
                .andExpect(jsonPath("$.errors.type").value("Transaction type must be 'Credit' or 'Debit'"));
    }

    @Test
    void getAllTransactions_Success() throws Exception {
        // Given
        when(transactionService.getAllTransactions(any(TransactionSearchRequest.class)))
            .thenReturn(pageResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Transactions retrieved successfully"))
                .andExpect(jsonPath("$.body.transactions").isArray())
                .andExpect(jsonPath("$.body.transactions[0].id").value(1))
                .andExpect(jsonPath("$.body.transactions[0].type").value("Credit"))
                .andExpect(jsonPath("$.body.currentPage").value(0))
                .andExpect(jsonPath("$.body.totalPages").value(1))
                .andExpect(jsonPath("$.body.totalElements").value(1))
                .andExpect(jsonPath("$.body.size").value(10))
                .andExpect(jsonPath("$.body.first").value(true))
                .andExpect(jsonPath("$.body.last").value(true));
    }

    @Test
    void getAllTransactions_WithFilters_Success() throws Exception {
        // Given
        when(transactionService.getAllTransactions(any(TransactionSearchRequest.class)))
            .thenReturn(pageResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/transactions")
                .param("type", "Credit")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "createdAt")
                .param("sortDirection", "DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Transactions retrieved successfully"))
                .andExpect(jsonPath("$.body.transactions").isArray())
                .andExpect(jsonPath("$.body.currentPage").value(0));
    }

    @Test
    void searchTransactions_WithValidRequest_Success() throws Exception {
        // Given
        TransactionIndexRequest searchRequest = TransactionIndexRequest.builder()
            .type("Credit")
            .page(0)
            .size(10)
            .sortBy("createdAt")
            .sortDirection("DESC")
            .build();

        TransactionResponse transactionResponse = TransactionResponse.builder()
            .id(1L)
            .amount(new BigDecimal("100.50"))
            .type("Credit")
            .note("Test transaction")
            .createdAt(LocalDateTime.now())
            .build();

        TransactionPageResponse pageResponse = TransactionPageResponse.builder()
            .transactions(Arrays.asList(transactionResponse))
            .currentPage(0)
            .totalPages(1)
            .totalElements(1L)
            .size(10)
            .first(true)
            .last(true)
            .build();

        when(transactionService.getAllTransactions(any(TransactionSearchRequest.class)))
            .thenReturn(pageResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Transactions retrieved successfully"))
                .andExpect(jsonPath("$.body.transactions").isArray())
                .andExpect(jsonPath("$.body.transactions.length()").value(1))
                .andExpect(jsonPath("$.body.transactions[0].id").value(1))
                .andExpect(jsonPath("$.body.transactions[0].type").value("Credit"))
                .andExpect(jsonPath("$.body.totalElements").value(1))
                .andExpect(jsonPath("$.body.currentPage").value(0));
    }

    @Test
    void searchTransactions_WithInvalidType_ValidationError() throws Exception {
        // Given
        TransactionIndexRequest searchRequest = TransactionIndexRequest.builder()
            .type("InvalidType")
            .page(0)
            .size(10)
            .build();

        // When & Then
        mockMvc.perform(post("/api/v1/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid request data"))
                .andExpect(jsonPath("$.errors.type").value("Transaction type must be 'Credit' or 'Debit'"));
    }

    @Test
    void getTransactionTotals_Success() throws Exception {
        // Given
        TransactionTotalsResponse totalsResponse = TransactionTotalsResponse.builder()
                .totalCreditAmount(new BigDecimal("1000.00"))
                .totalDebitAmount(new BigDecimal("750.50"))
                .totalCreditCount(5L)
                .totalDebitCount(3L)
                .netAmount(new BigDecimal("249.50"))
                .totalTransactionCount(8L)
                .build();

        when(transactionService.getTransactionTotals()).thenReturn(totalsResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/transactions/totals"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Transaction totals retrieved successfully"))
                .andExpect(jsonPath("$.body.totalCreditAmount").value(1000.00))
                .andExpect(jsonPath("$.body.totalDebitAmount").value(750.50))
                .andExpect(jsonPath("$.body.totalCreditCount").value(5))
                .andExpect(jsonPath("$.body.totalDebitCount").value(3))
                .andExpect(jsonPath("$.body.netAmount").value(249.50))
                .andExpect(jsonPath("$.body.totalTransactionCount").value(8));
    }
}
