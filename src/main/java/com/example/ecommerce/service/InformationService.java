package com.example.ecommerce.service;

import com.example.ecommerce.dto.response.BannerResponse;
import com.example.ecommerce.dto.response.ResponseDTO;
import com.example.ecommerce.dto.response.LayananResponse;

import java.util.List;

public interface InformationService {
    ResponseDTO<List<BannerResponse>> bannerAll();
    ResponseDTO<List<LayananResponse>> serviceAll(String token);
}
