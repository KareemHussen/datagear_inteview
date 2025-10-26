package com.datagear.DataGearInterview.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for paginated transaction response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionPageResponse {

    private List<TransactionResponse> transactions;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int size;
    private boolean first;
    private boolean last;
}
