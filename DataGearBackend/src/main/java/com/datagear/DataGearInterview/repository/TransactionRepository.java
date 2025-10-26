package com.datagear.DataGearInterview.repository;

import com.datagear.DataGearInterview.dto.projection.TransactionTotalsProjection;
import com.datagear.DataGearInterview.entity.Transaction;
import com.datagear.DataGearInterview.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for Transaction entity
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    /**
     * Find transactions by type with pagination
     * @param type transaction type
     * @param pageable pagination information
     * @return page of transactions
     */
    Page<Transaction> findByType(TransactionType type, Pageable pageable);

    /**
     * Find transactions created between two dates with pagination
     * @param fromDate start date
     * @param toDate end date
     * @param pageable pagination information
     * @return page of transactions
     */
    Page<Transaction> findByCreatedAtBetween(java.time.LocalDateTime fromDate, java.time.LocalDateTime toDate, Pageable pageable);

    /**
     * Find all transactions by type (for totals calculation)
     * @param type transaction type
     * @return list of transactions
     */
    List<Transaction> findByType(TransactionType type);

    /**
     * Count transactions by type (optimized for performance)
     * @param type transaction type
     * @return count of transactions
     */
    long countByType(TransactionType type);

    /**
     * Get sum of amounts for transactions by type (optimized JPQL query)
     * @param type transaction type
     * @return sum of transaction amounts for the given type
     */
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = :type")
    BigDecimal getTotalAmountByType(@Param("type") TransactionType type);

    /**
     * Get all transaction totals in a single optimized query
     * @return transaction totals projection with all aggregated data
     */
    @Query("""
        SELECT 
            COALESCE(SUM(CASE WHEN t.type = 'CREDIT' THEN t.amount ELSE 0 END), 0) as totalCreditAmount,
            COALESCE(SUM(CASE WHEN t.type = 'DEBIT' THEN t.amount ELSE 0 END), 0) as totalDebitAmount,
            COUNT(CASE WHEN t.type = 'CREDIT' THEN 1 END) as totalCreditCount,
            COUNT(CASE WHEN t.type = 'DEBIT' THEN 1 END) as totalDebitCount
        FROM Transaction t
        """)
    TransactionTotalsProjection getTransactionTotals();
}
