package com.datagear.DataGearInterview.mapper;

import com.datagear.DataGearInterview.dto.request.TransactionCreateRequest;
import com.datagear.DataGearInterview.dto.response.TransactionResponse;
import com.datagear.DataGearInterview.entity.Transaction;
import com.datagear.DataGearInterview.enums.TransactionType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * MapStruct mapper for Transaction entity and DTOs
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper {

    /**
     * Convert TransactionCreateRequest to Transaction entity
     * @param request transaction create request
     * @return transaction entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "type", source = "type", qualifiedByName = "stringToTransactionType")
    Transaction toEntity(TransactionCreateRequest request);

    /**
     * Convert Transaction entity to TransactionResponse
     * @param transaction transaction entity
     * @return transaction response
     */
    @Mapping(target = "type", source = "type", qualifiedByName = "transactionTypeToString")
    TransactionResponse toResponse(Transaction transaction);

    /**
     * Convert string type to TransactionType enum
     * @param typeString transaction type string
     * @return transaction type enum
     */
    @Named("stringToTransactionType")
    default TransactionType stringToTransactionType(String typeString) {
        return TransactionType.fromString(typeString);
    }

    /**
     * Convert TransactionType enum to string
     * @param transactionType transaction type enum
     * @return transaction type string
     */
    @Named("transactionTypeToString")
    default String transactionTypeToString(TransactionType transactionType) {
        return transactionType.getStringValue();
    }
}
