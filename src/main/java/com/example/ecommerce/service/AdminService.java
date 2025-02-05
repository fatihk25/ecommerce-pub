package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.LoginAdminRequest;
import com.example.ecommerce.dto.response.ResponseDTO;
import com.example.ecommerce.dto.response.TokenDTO;
import com.example.ecommerce.entity.Admin;
import com.example.ecommerce.entity.Layanan;

public interface AdminService {
    ResponseDTO<Void> createAdmin (Admin admin);
    ResponseDTO<TokenDTO> loginAdmin(LoginAdminRequest request);
    ResponseDTO<Layanan> createProduct(String token, Layanan layanan);
}
