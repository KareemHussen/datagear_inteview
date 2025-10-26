package com.datagear.DataGearInterview.service;

import com.datagear.DataGearInterview.dto.request.TransactionCreateRequest;
import com.datagear.DataGearInterview.dto.request.TransactionSearchRequest;
import com.datagear.DataGearInterview.dto.response.TransactionPageResponse;
import com.datagear.DataGearInterview.dto.response.TransactionResponse;
import com.datagear.DataGearInterview.entity.Transaction;
import com.datagear.DataGearInterview.enums.TransactionType;
import com.datagear.DataGearInterview.mapper.TransactionMapper;
import com.datagear.DataGearInterview.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TransactionService
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

   @Mock
   private TransactionRepository transactionRepository;

   @Mock
   private TransactionMapper transactionMapper;

   private TransactionService transactionService;

   private TransactionCreateRequest createRequest;
   private Transaction transaction;
   private TransactionResponse transactionResponse;

   @BeforeEach
   void setUp() {
       // Manually inject the mapper since MapStruct doesn't generate Spring beans in test environment
       transactionService = new TransactionService(transactionRepository, transactionMapper);

       createRequest = TransactionCreateRequest.builder()
           .amount(new BigDecimal("100.50"))
           .type("Credit")
           .note("Test transaction")
           .build();

       transaction = Transaction.builder()
           .id(1L)
           .amount(new BigDecimal("100.50"))
           .type(TransactionType.CREDIT)
           .note("Test transaction")
           .createdAt(LocalDateTime.now())
           .build();

       transactionResponse = TransactionResponse.builder()
           .id(1L)
           .amount(new BigDecimal("100.50"))
           .type("Credit")
           .note("Test transaction")
           .createdAt(LocalDateTime.now())
           .build();
   }

   @Test
   void createTransaction_Success() {
       // Given
       when(transactionMapper.toEntity(any(TransactionCreateRequest.class))).thenReturn(transaction);
       when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
       when(transactionMapper.toResponse(any(Transaction.class))).thenReturn(transactionResponse);

       // When
       TransactionResponse result = transactionService.createTransaction(createRequest);

       // Then
       assertNotNull(result);
       assertEquals(1L, result.getId());
       assertEquals("Credit", result.getType());
       assertEquals(new BigDecimal("100.50"), result.getAmount());
       assertEquals("Test transaction", result.getNote());

       verify(transactionRepository).save(any(Transaction.class));
   }

   @Test
   void createTransaction_WithDebitType_Success() {
       // Given
       createRequest.setType("Debit");
       transaction.setType(TransactionType.DEBIT);
       transactionResponse.setType("Debit");

       when(transactionMapper.toEntity(any(TransactionCreateRequest.class))).thenReturn(transaction);
       when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
       when(transactionMapper.toResponse(any(Transaction.class))).thenReturn(transactionResponse);

       // When
       TransactionResponse result = transactionService.createTransaction(createRequest);

       // Then
       assertNotNull(result);
       assertEquals("Debit", result.getType());
       verify(transactionRepository).save(any(Transaction.class));
   }

   @Test
   void getAllTransactions_WithPagination_Success() {
       // Given
       TransactionSearchRequest searchRequest = TransactionSearchRequest.builder()
           .page(0)
           .size(10)
           .build();

       List<Transaction> transactions = Arrays.asList(transaction);
       Page<Transaction> transactionPage = new PageImpl<>(transactions, PageRequest.of(0, 10), 1);

       when(transactionRepository.findAll(any(Specification.class), any(Pageable.class)))
           .thenReturn(transactionPage);

       // When
       TransactionPageResponse result = transactionService.getAllTransactions(searchRequest);

       // Then
       assertNotNull(result);
       assertEquals(1, result.getTransactions().size());
       assertEquals(0, result.getCurrentPage());
       assertEquals(1, result.getTotalPages());
       assertEquals(1, result.getTotalElements());
       assertEquals(10, result.getSize());
       assertTrue(result.isFirst());
       assertTrue(result.isLast());

       verify(transactionRepository).findAll(any(Specification.class), any(Pageable.class));
   }

   @Test
   void getAllTransactions_WithTypeFilter_Success() {
       // Given
       TransactionSearchRequest searchRequest = TransactionSearchRequest.builder()
           .type("Credit")
           .page(0)
           .size(10)
           .build();

       List<Transaction> transactions = Arrays.asList(transaction);
       Page<Transaction> transactionPage = new PageImpl<>(transactions, PageRequest.of(0, 10), 1);

       when(transactionRepository.findAll(any(Specification.class), any(Pageable.class)))
           .thenReturn(transactionPage);

       // When
       TransactionPageResponse result = transactionService.getAllTransactions(searchRequest);

       // Then
       assertNotNull(result);
       assertEquals(1, result.getTransactions().size());
       verify(transactionRepository).findAll(any(Specification.class), any(Pageable.class));
   }

   @Test
   void getAllTransactions_WithInvalidType_ThrowsException() {
       // Given
       TransactionSearchRequest searchRequest = TransactionSearchRequest.builder()
               .type("InvalidType")
               .page(0)
               .size(10)
               .build();

       // When & Then
       IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
               transactionService.getAllTransactions(searchRequest)
       );

       assertEquals("Invalid transaction type: InvalidType. Must be 'Credit' or 'Debit'", exception.getMessage());
   }

   @Test
   void getAllTransactions_WithDateRange_Success() {
       // Given
       LocalDateTime fromDate = LocalDateTime.now().minusDays(7);
       LocalDateTime toDate = LocalDateTime.now();

       TransactionSearchRequest searchRequest = TransactionSearchRequest.builder()
               .fromDate(fromDate)
               .toDate(toDate)
               .page(0)
               .size(10)
               .build();

       List<Transaction> transactions = Arrays.asList(transaction);
       Page<Transaction> transactionPage = new PageImpl<>(transactions, PageRequest.of(0, 10), 1);

       when(transactionRepository.findAll(
               any(Specification.class),
               any(Pageable.class)))
               .thenReturn(transactionPage);

       // When
       TransactionPageResponse result = transactionService.getAllTransactions(searchRequest);

       // Then
       assertNotNull(result);
       assertEquals(1, result.getTransactions().size());
       verify(transactionRepository).findAll(
               any(Specification.class),
               any(Pageable.class));
   }


}
