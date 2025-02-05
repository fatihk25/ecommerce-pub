package com.example.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LayananResponse {
    @JsonProperty("service_code")
    private String serviceCode;

    @JsonProperty("service_name")
    private String serviceName;

    @JsonProperty("service_icon")
    private String serviceIcon;

    @JsonProperty("service_tariff")
    private Integer serviceTariff;
}
