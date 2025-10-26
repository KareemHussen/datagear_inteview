package com.datagear.DataGearInterview.controller;

import com.datagear.DataGearInterview.dto.request.TransactionCreateRequest;
import com.datagear.DataGearInterview.dto.request.TransactionSearchRequest;
import com.datagear.DataGearInterview.dto.response.APIResponse;
import com.datagear.DataGearInterview.dto.response.TransactionPageResponse;
import com.datagear.DataGearInterview.dto.response.TransactionResponse;
import com.datagear.DataGearInterview.dto.response.TransactionTotalsResponse;
import com.datagear.DataGearInterview.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Transaction operations with API versioning
 */
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Transaction Management", description = "APIs for managing financial transactions")
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Create a new transaction
     * @param request transaction create request
     * @return created transaction response
     */
    @PostMapping
    @Operation(
        summary = "Create a new transaction",
        description = "Creates a new financial transaction with the provided details"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Transaction created successfully",
            content = @Content(schema = @Schema(implementation = APIResponse.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid request data",
            content = @Content(schema = @Schema(implementation = APIResponse.class))
        )
    })
    public ResponseEntity<APIResponse<TransactionResponse>> createTransaction(
            @Valid @RequestBody TransactionCreateRequest request) {
        
        log.info("Received request to create transaction: {}", request);

        TransactionResponse response = transactionService.createTransaction(request);

        log.info("Transaction created successfully with ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success(response, "Transaction created successfully"));

    }

    /**
     * Get all transactions with pagination and filtering
     *
     @param searchRequest search criteria
     */
    @GetMapping
    @Operation(
        summary = "Get all transactions",
        description = "Retrieves all transactions with optional filtering and pagination"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Transactions retrieved successfully",
            content = @Content(schema = @Schema(implementation = APIResponse.class))
        )
    })
    public ResponseEntity<APIResponse<TransactionPageResponse>> getAllTransactions(@Valid TransactionSearchRequest searchRequest) {
        
       log.info("Received request to get transactions with filters - type: {}, fromDate: {}, toDate: {}, sortBy: {}, sortDirection: {}, page: {}, size: {}",
               searchRequest.getType() ,searchRequest.getFromDate(), searchRequest.getToDate(), searchRequest.getSortBy(), searchRequest.getSortDirection(), searchRequest.getPage(), searchRequest.getSize());
        
        
        TransactionPageResponse response = transactionService.getAllTransactions(searchRequest);
        
        log.info("Retrieved {} transactions out of {} total", 
            response.getTransactions().size(), 
            response.getTotalElements());
        
        return ResponseEntity.ok(APIResponse.success(response, "Transactions retrieved successfully"));
    }

    /**
     * Get transaction totals (sum and count of credit/debit transactions)
     * @return transaction totals response
     */
    @GetMapping("/totals")
    @Operation(
        summary = "Get transaction totals",
        description = "Retrieves the total sum and count of credit and debit transactions"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved transaction totals",
            content = @Content(schema = @Schema(implementation = APIResponse.class))
        )
    })
    public ResponseEntity<APIResponse<TransactionTotalsResponse>> getTransactionTotals() {
        log.info("Received request to get transaction totals");
        
        TransactionTotalsResponse response = transactionService.getTransactionTotals();
        
        log.info("Retrieved transaction totals - Credit: {} ({}), Debit: {} ({}), Net: {}", 
                response.getTotalCreditAmount(), response.getTotalCreditCount(),
                response.getTotalDebitAmount(), response.getTotalDebitCount(), response.getNetAmount());
        
        return ResponseEntity.ok(APIResponse.success(response, "Transaction totals retrieved successfully"));
    }

 
}
