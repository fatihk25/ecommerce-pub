package com.example.ecommerce.dto.request;

import com.example.ecommerce.dto.response.TransactionHistoryResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionHistoryWrapper {
    private int offset;
    private int limit;
    private List<TransactionHistoryResponse> records;
}
