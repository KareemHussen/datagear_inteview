package com.datagear.DataGearInterview.dto.projection;

import java.math.BigDecimal;

/**
 * Projection interface for transaction totals aggregation
 */
public interface TransactionTotalsProjection {
    
    /**
     * Get total credit amount
     * @return total credit amount
     */
    BigDecimal getTotalCreditAmount();
    
    /**
     * Get total debit amount
     * @return total debit amount
     */
    BigDecimal getTotalDebitAmount();
    
    /**
     * Get total credit count
     * @return total credit count
     */
    Long getTotalCreditCount();
    
    /**
     * Get total debit count
     * @return total debit count
     */
    Long getTotalDebitCount();
}
