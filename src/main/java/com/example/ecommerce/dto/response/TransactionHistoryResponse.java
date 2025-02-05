package com.example.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionHistoryResponse {

    @JsonProperty("invoice_number")
    private String invoiceNumber;

    @JsonProperty("transaction_type")
    private String transactionType;

    private String description;
    private Integer totalAmount;
    private LocalDateTime createdOn;
}
