package com.datagear.DataGearInterview.repository;

import com.datagear.DataGearInterview.dto.request.TransactionSearchRequest;
import com.datagear.DataGearInterview.entity.Transaction;
import com.datagear.DataGearInterview.enums.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * Specifications for Transaction entity filtering and sorting
 */
public class TransactionSpecifications {


    private static final Logger log = LoggerFactory.getLogger(TransactionSpecifications.class);

    private TransactionSpecifications() {
    }

    /**
     * Filter by transaction type (CREDIT/DEBIT)
     */
    public static Specification<Transaction> hasType(TransactionType type) {
        return (root, query, cb) ->
                type == null ? cb.conjunction() : cb.equal(root.get("type"), type);
    }

    /**
     * Filter by date range
     */
    public static Specification<Transaction> hasDateRange(LocalDateTime fromDate, LocalDateTime toDate) {
        return (root, query, cb) -> {

            if (fromDate == null && toDate == null) return cb.conjunction();

            if (fromDate != null && toDate != null) {
                return cb.between(root.get("c" +
                        "reatedAt"), fromDate, toDate);
            } else if (fromDate != null) {
                return cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate);
            } else {
                return cb.lessThanOrEqualTo(root.get("createdAt"), toDate);
            }
        };
    }

    /**
     * Filter by minimum amount
     */
    public static Specification<Transaction> hasMinAmount(BigDecimal minAmount) {
        return (root, query, cb) ->
                minAmount == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("amount"), minAmount);
    }

    /**
     * Filter by maximum amount
     */
    public static Specification<Transaction> hasMaxAmount(BigDecimal maxAmount) {
        return (root, query, cb) ->
                maxAmount == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("amount"), maxAmount);
    }

    /**
     * Combine multiple Specifications
     */
    public static Specification<Transaction> build(TransactionSearchRequest request) {
        Specification<Transaction> spec = Specification.unrestricted();

        if (request.getType() != null && !request.getType().isBlank()) {
            spec = spec.and(hasType(TransactionType.fromString(request.getType())));
        }

        spec = spec
                .and(hasDateRange(request.getFromDate(), request.getToDate()))
                .and(hasMinAmount(request.getMinAmount()))
                .and(hasMaxAmount(request.getMaxAmount()));

        return spec;
    }

}
