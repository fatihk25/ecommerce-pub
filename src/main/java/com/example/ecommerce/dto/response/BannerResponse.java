package com.example.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BannerResponse {
    @JsonProperty("banner_name")
    private String bannerName;

    @JsonProperty("banner_image")
    private String bannerImage;

    private String description;
}
