package com.datagear.DataGearInterview.service;

import com.datagear.DataGearInterview.dto.request.TransactionCreateRequest;
import com.datagear.DataGearInterview.dto.request.TransactionSearchRequest;
import com.datagear.DataGearInterview.dto.response.TransactionPageResponse;
import com.datagear.DataGearInterview.dto.response.TransactionResponse;
import com.datagear.DataGearInterview.dto.response.TransactionTotalsResponse;
import com.datagear.DataGearInterview.entity.Transaction;
import com.datagear.DataGearInterview.mapper.TransactionMapper;
import com.datagear.DataGearInterview.repository.TransactionRepository;
import com.datagear.DataGearInterview.repository.TransactionSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service class for Transaction business logic
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    /**
     * Create a new transaction
     * @param request transaction create request
     * @return created transaction response
     */
    @CacheEvict(value = "transactionTotals", allEntries = true)
    public TransactionResponse createTransaction(TransactionCreateRequest request) {

        log.info("Creating new transaction with amount: {} and type: {}", request.getAmount(), request.getType());
        
        Transaction transaction = transactionMapper.toEntity(request);
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        log.info("Transaction created successfully with ID: {}", savedTransaction.getId());
        
        return transactionMapper.toResponse(savedTransaction);
    }

    /**
     * Get all transactions with pagination and filtering
     * @param searchRequest search criteria
     * @return paginated transaction response
     */
    @Transactional(readOnly = true)
    public TransactionPageResponse getAllTransactions(TransactionSearchRequest searchRequest) {
        log.info("Fetching transactions with criteria: {}", searchRequest);

        Specification<Transaction> specification = TransactionSpecifications.build(searchRequest);
        Pageable pageable = createPageable(searchRequest);

        Page<Transaction> transactionPage = transactionRepository.findAll(specification, pageable);

        log.info("Found {} transactions out of {} total", 
            transactionPage.getNumberOfElements(), 
            transactionPage.getTotalElements());


        // Convert Page to TransactionPageResponse
        return buildTransactionPageResponse(transactionPage);
    }

    /**
     * Create pageable object with sorting
     * @param searchRequest search criteria
     * @return pageable object
     */
    private Pageable createPageable(TransactionSearchRequest searchRequest) {
        int page = searchRequest.getPageOrDefault();
        int size = searchRequest.getSizeOrDefault();
        
        Sort sort = createSort(searchRequest);
        
        return PageRequest.of(page, size, sort);
    }


    /**
     * Create sort object based on search criteria
     * @param searchRequest search criteria
     * @return sort object
     */
    private Sort createSort(TransactionSearchRequest searchRequest) {
        String sortBy = searchRequest.getSortByOrDefault();
        String sortDirection = searchRequest.getSortDirectionOrDefault();

        // Determine sort direction
        Sort.Direction direction =
                "ASC".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;

        // Build and return Sort object
        return Sort.by(direction, sortBy);
    }

    /**
     * Build transaction page response
     * @param transactionPage page of transactions
     * @return transaction page response
     */
    private TransactionPageResponse buildTransactionPageResponse(Page<Transaction> transactionPage) {
        List<TransactionResponse> transactionResponses = transactionPage.getContent()
            .stream()
            .map(transactionMapper::toResponse)
            .toList();
        
        return TransactionPageResponse.builder()
            .transactions(transactionResponses)
            .currentPage(transactionPage.getNumber())
            .totalPages(transactionPage.getTotalPages())
            .totalElements(transactionPage.getTotalElements())
            .size(transactionPage.getSize())
            .first(transactionPage.isFirst())
            .last(transactionPage.isLast())
            .build();
    }


    /**
     * Get transaction totals (sum and count of credit/debit transactions)
     * Optimized using single query with projection for maximum performance
     * @return transaction totals response
     */
    @Cacheable(value = "transactionTotals")
    @Transactional(readOnly = true)
    public TransactionTotalsResponse getTransactionTotals() {
        log.info("Fetching transaction totals using single optimized query");

        // Get all totals in a single database query
        var totals = transactionRepository.getTransactionTotals();

        // Extract values from projection
        BigDecimal totalCreditAmount = totals.getTotalCreditAmount();
        BigDecimal totalDebitAmount = totals.getTotalDebitAmount();
        Long totalCreditCount = totals.getTotalCreditCount();
        Long totalDebitCount = totals.getTotalDebitCount();

        // Calculate net amount and total count
        BigDecimal netAmount = totalCreditAmount.subtract(totalDebitAmount);
        Long totalTransactionCount = totalCreditCount + totalDebitCount;

        log.info("Transaction totals - Credit: {} ({}), Debit: {} ({}), Net: {}", 
                totalCreditAmount, totalCreditCount, totalDebitAmount, totalDebitCount, netAmount);

        return TransactionTotalsResponse.builder()
                .totalCreditAmount(totalCreditAmount)
                .totalDebitAmount(totalDebitAmount)
                .totalCreditCount(totalCreditCount)
                .totalDebitCount(totalDebitCount)
                .netAmount(netAmount)
                .totalTransactionCount(totalTransactionCount)
                .build();
    }
}
