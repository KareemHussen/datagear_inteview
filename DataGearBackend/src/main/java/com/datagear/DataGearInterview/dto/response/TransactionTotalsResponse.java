package com.datagear.DataGearInterview.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for transaction totals response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionTotalsResponse {
    private BigDecimal totalCreditAmount;
    private BigDecimal totalDebitAmount;
    private Long totalCreditCount;
    private Long totalDebitCount;
    private BigDecimal netAmount; // totalCreditAmount - totalDebitAmount
    private Long totalTransactionCount;
}
