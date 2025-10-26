package com.datagear.DataGearInterview.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for creating a new transaction
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCreateRequest {

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    @DecimalMax(value = "90000000", message = "Amount cannot exceed 90,000,000")
    private BigDecimal amount;

    @NotBlank(message = "Transaction type cannot be blank")
    @Pattern(regexp = "^(Credit|Debit)$", message = "Transaction type must be 'Credit' or 'Debit'")
    private String type; // "Credit" or "Debit"

    private String note;
}
