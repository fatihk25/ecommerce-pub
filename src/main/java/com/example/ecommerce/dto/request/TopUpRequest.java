package com.example.ecommerce.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TopUpRequest {
    @JsonProperty("top_up_amount")
    private Integer topUpAmount;
}
