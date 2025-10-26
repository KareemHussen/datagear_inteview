package com.datagear.DataGearInterview.dto.request;

import jakarta.validation.constraints.*;
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
    private BigDecimal amount;

    @NotBlank(message = "Transaction type cannot be blank")
    @Pattern(regexp = "^(Credit|Debit)$", message = "Transaction type must be 'Credit' or 'Debit'")
    private String type; // "Credit" or "Debit"

    private String note;
}
