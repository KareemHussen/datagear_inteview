package com.datagear.DataGearInterview.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for transaction search/filter criteria
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSearchRequest {

    @Pattern(regexp = "^(Credit|Debit)$", message = "Type must be 'Credit' or 'Debit'")
    private String type; // "Credit" or "Debit"

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
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

    @DecimalMin(value = "0.01", message = "Min amount must be 1 or greater")
    private BigDecimal minAmount;

    @DecimalMin(value = "0.01", message = "Max amount must be 1 or greater")
    private BigDecimal maxAmount;

    /**
     * Get default page number if not specified
     */
    @Schema(hidden = true)
    public Integer getPageOrDefault() {
        return page != null ? page : 0;
    }

    /**
     * Get default page size if not specified
     */
    @Schema(hidden = true)
    public Integer getSizeOrDefault() {
        return size != null ? size : 10;
    }

    /**
     * Get default sort direction if not specified
     */
    @Schema(hidden = true)
    public String getSortDirectionOrDefault() {
        return sortDirection != null ? sortDirection : "DESC";
    }

    /**
     * Get default sort by if not specified
     */
    @Schema(hidden = true)
    public String getSortByOrDefault() {
        return sortBy != null ? sortBy : "createdAt";
    }

}
