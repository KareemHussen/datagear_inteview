package com.datagear.DataGearInterview.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;

/**
 * DTO for transaction index/search request
 * This can be used as a request body for POST /api/v1/transactions
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionIndexRequest {

    @Pattern(regexp = "^(Credit|Debit)$", message = "Type must be 'Credit' or 'Debit'")
    private String type; // "Credit" or "Debit"
    
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    
    @Pattern(regexp = "^(id|amount|createdAt|type|note)$", message = "Sort by must be one of: id, amount, createdAt, type, note")
    private String sortBy; // column name for sorting
    
    @Pattern(regexp = "^(ASC|DESC)$", message = "Sort direction must be 'ASC' or 'DESC'")
    private String sortDirection; // "ASC" or "DESC"
    
    @Min(value = 0, message = "Page number must be 0 or greater")
    private Integer page;
    
    @Min(value = 1, message = "Page size must be 1 or greater")
    @Max(value = 100, message = "Page size cannot exceed 100")
    private Integer size;

    /**
     * Get default page number if not specified
     * @return page number (default: 0)
     */
    public Integer getPageOrDefault() {
        return page != null ? page : 0;
    }

    /**
     * Get default page size if not specified
     * @return page size (default: 10)
     */
    public Integer getSizeOrDefault() {
        return size != null ? size : 10;
    }

    /**
     * Get default sort direction if not specified
     * @return sort direction (default: "DESC")
     */
    public String getSortDirectionOrDefault() {
        return StringUtils.hasText(sortDirection) ? sortDirection : "DESC";
    }

    /**
     * Get default sort column if not specified
     * @return sort column (default: "createdAt")
     */
    public String getSortByOrDefault() {
        return StringUtils.hasText(sortBy) ? sortBy : "createdAt";
    }
}
