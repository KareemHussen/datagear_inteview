package com.datagear.DataGearInterview;

import com.datagear.DataGearInterview.dto.request.TransactionCreateRequest;
import com.datagear.DataGearInterview.dto.response.APIResponse;
import com.datagear.DataGearInterview.dto.response.TransactionPageResponse;
import com.datagear.DataGearInterview.dto.response.TransactionResponse;
import com.datagear.DataGearInterview.repository.TransactionRepository;
import com.datagear.DataGearInterview.dto.response.TransactionTotalsResponse;
import com.datagear.DataGearInterview.entity.Transaction;
import com.datagear.DataGearInterview.enums.TransactionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the complete transaction flow
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TransactionIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void cleanDb() {
        transactionRepository.deleteAll();
    }

    @Test
    void createAndRetrieveTransaction_CompleteFlow_Success() {
        // Given
        TransactionCreateRequest createRequest = TransactionCreateRequest.builder()
            .amount(new BigDecimal("250.75"))
            .type("Debit")
            .note("Integration test transaction")
            .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TransactionCreateRequest> requestEntity = new HttpEntity<>(createRequest, headers);

        // When - Create transaction
        ResponseEntity<APIResponse> createResponse = restTemplate.exchange(
            "http://localhost:" + port + "/api/v1/transactions",
            HttpMethod.POST,
            requestEntity,
            APIResponse.class
        );

        System.out.println();
        // Then - Verify creation
        assertEquals(201, createResponse.getStatusCodeValue());
        APIResponse apiResponse = createResponse.getBody();
        assertNotNull(apiResponse);
        assertTrue(apiResponse.isSuccess());
        assertEquals("Transaction created successfully", apiResponse.getMessage());
        
        // Parse the body as a Map and extract the transaction data
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> bodyMap = (java.util.Map<String, Object>) apiResponse.getBody();
        assertNotNull(bodyMap);
        assertEquals(0, new BigDecimal("250.75").compareTo(new BigDecimal(bodyMap.get("amount").toString())));
        assertEquals("Debit", bodyMap.get("type"));
        assertEquals("Integration test transaction", bodyMap.get("note"));
        assertNotNull(bodyMap.get("id"));
        assertNotNull(bodyMap.get("createdAt"));

        // Verify transaction was saved in database
        Transaction savedTransaction = transactionRepository.findAll().get(0);
        assertEquals(new BigDecimal("250.75"), savedTransaction.getAmount());
        assertEquals(TransactionType.DEBIT, savedTransaction.getType());
        assertEquals("Integration test transaction", savedTransaction.getNote());
        assertNotNull(savedTransaction.getCreatedAt());

        // When - Retrieve all transactions
        ResponseEntity<APIResponse> getResponse = restTemplate.exchange(
            "http://localhost:" + port + "/api/v1/transactions",
            HttpMethod.GET,
            null,
            APIResponse.class
        );

        // Then - Verify retrieval
        assertEquals(200, getResponse.getStatusCodeValue());
        APIResponse getApiResponse = getResponse.getBody();
        assertNotNull(getApiResponse);
        assertTrue(getApiResponse.isSuccess());
        assertEquals("Transactions retrieved successfully", getApiResponse.getMessage());
        
        // Parse the body as a Map and extract the page response data
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> getBodyMap = (java.util.Map<String, Object>) getApiResponse.getBody();
        assertNotNull(getBodyMap);
        
        @SuppressWarnings("unchecked")
        java.util.List<java.util.Map<String, Object>> transactions = (java.util.List<java.util.Map<String, Object>>) getBodyMap.get("transactions");
        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(0, new BigDecimal("250.75").compareTo(new BigDecimal(transactions.get(0).get("amount").toString())));
        assertEquals("Debit", transactions.get(0).get("type"));
        assertEquals(1L, ((Number) getBodyMap.get("totalElements")).longValue());
        assertEquals(0, ((Number) getBodyMap.get("currentPage")).intValue());
        assertEquals(1, ((Number) getBodyMap.get("totalPages")).intValue());
    }

    @Test
    void createTransaction_WithCreditType_Success() {
        // Given
        TransactionCreateRequest createRequest = TransactionCreateRequest.builder()
            .amount(new BigDecimal("100.00"))
            .type("Credit")
            .note("Credit transaction")
            .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TransactionCreateRequest> requestEntity = new HttpEntity<>(createRequest, headers);

        // When
        ResponseEntity<APIResponse> response = restTemplate.exchange(
            "http://localhost:" + port + "/api/v1/transactions",
            HttpMethod.POST,
            requestEntity,
            APIResponse.class
        );

        // Then
        assertEquals(201, response.getStatusCodeValue());
        APIResponse apiResponse = response.getBody();
        assertNotNull(apiResponse);
        assertTrue(apiResponse.isSuccess());
        assertEquals("Transaction created successfully", apiResponse.getMessage());
        
        // Parse the body as a Map and extract the transaction data
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> bodyMap = (java.util.Map<String, Object>) apiResponse.getBody();
        assertNotNull(bodyMap);
        assertEquals("Credit", bodyMap.get("type"));
        assertEquals(0, new BigDecimal("100.00").compareTo(new BigDecimal(bodyMap.get("amount").toString())));

        // Verify in database - get the latest transaction (should be the one we just created)
        List<Transaction> allTransactions = transactionRepository.findAll();
        Transaction savedTransaction = allTransactions.get(allTransactions.size() - 1);
        assertEquals(TransactionType.CREDIT, savedTransaction.getType());
        assertEquals(new BigDecimal("100.00"), savedTransaction.getAmount());
    }

    @Test
    void getTransactionTotals_WithTransactions_Success() {
        // Given - Create some test transactions
        TransactionCreateRequest creditRequest = TransactionCreateRequest.builder()
                .amount(new BigDecimal("500.00"))
                .type("Credit")
                .note("Credit transaction for totals test")
                .build();

        TransactionCreateRequest debitRequest = TransactionCreateRequest.builder()
                .amount(new BigDecimal("300.00"))
                .type("Debit")
                .note("Debit transaction for totals test")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create credit transaction
        HttpEntity<TransactionCreateRequest> creditEntity = new HttpEntity<>(creditRequest, headers);
        restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/transactions",
                HttpMethod.POST,
                creditEntity,
                APIResponse.class
        );

        // Create debit transaction
        HttpEntity<TransactionCreateRequest> debitEntity = new HttpEntity<>(debitRequest, headers);
        restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/transactions",
                HttpMethod.POST,
                debitEntity,
                APIResponse.class
        );

        // When - Get transaction totals
        ResponseEntity<APIResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/transactions/totals",
                HttpMethod.GET,
                null,
                APIResponse.class
        );

        // Then - Verify totals
        assertEquals(200, response.getStatusCodeValue());
        APIResponse apiResponse = response.getBody();
        assertNotNull(apiResponse);
        assertTrue(apiResponse.isSuccess());
        assertEquals("Transaction totals retrieved successfully", apiResponse.getMessage());
        
        // Parse the body as a Map and extract the totals data
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> bodyMap = (java.util.Map<String, Object>) apiResponse.getBody();
        assertNotNull(bodyMap);
        assertEquals(0, new BigDecimal("500.00").compareTo(new BigDecimal(bodyMap.get("totalCreditAmount").toString())));
        assertEquals(0, new BigDecimal("300.00").compareTo(new BigDecimal(bodyMap.get("totalDebitAmount").toString())));
        assertEquals(1L, ((Number) bodyMap.get("totalCreditCount")).longValue());
        assertEquals(1L, ((Number) bodyMap.get("totalDebitCount")).longValue());
        assertEquals(0, new BigDecimal("200.00").compareTo(new BigDecimal(bodyMap.get("netAmount").toString())));
        assertEquals(2L, ((Number) bodyMap.get("totalTransactionCount")).longValue());
    }
}
