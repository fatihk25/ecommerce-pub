package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.request.LoginAdminRequest;
import com.example.ecommerce.dto.response.ResponseDTO;
import com.example.ecommerce.dto.response.TokenDTO;
import com.example.ecommerce.entity.Admin;
import com.example.ecommerce.entity.Layanan;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.AdminRepository;
import com.example.ecommerce.repository.LayananRepository;
import com.example.ecommerce.security.JwtUtils;
import com.example.ecommerce.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private LayananRepository layananRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public ResponseDTO<Void> createAdmin(Admin admin) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";

        if (!admin.getEmail().matches(emailRegex)) {
            return new ResponseDTO<>(102, "Parameter email tidak sesuai format", null);
        }

        // Validasi password minimal 8 karakter
        if (admin.getPassword().length() < 8) {
            return new ResponseDTO<>(103, "Password harus memiliki panjang minimal 8 karakter", null);
        }

        // Cek email sudah terdaftar
        if (adminRepository.existsByEmail(admin.getEmail())) {
            return new ResponseDTO<>(104, "Email sudah terdaftar", null);
        }

        // Hash password dan simpan user
        String hashedPassword = new BCryptPasswordEncoder().encode(admin.getPassword());
        admin.setPassword(hashedPassword);
        adminRepository.save(admin);

        return new ResponseDTO<>(0, "Registrasi berhasil silahkan login", null);
    }

    @Override
    public ResponseDTO<TokenDTO> loginAdmin(LoginAdminRequest request) {
        Admin admin = adminRepository.findByEmail(request.getEmail());

        // Validasi Format Email
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!request.getEmail().matches(emailRegex)) {
            return new ResponseDTO<>(102, "Parameter email tidak sesuai format", null);
        }

        // Validasi Email
        if (admin == null) {
            return new ResponseDTO<>(102, "Username atau password salah", null);
        }

        // Validasi Password
        Boolean isPasswordMatch = new BCryptPasswordEncoder().matches(request.getPassword(), admin.getPassword());
        if (!new BCryptPasswordEncoder().matches(request.getPassword(), admin.getPassword())) {
            return new ResponseDTO<>(103, "Username atau password salah", null);
        }

        // Set Token
        TokenDTO tokenDTO = new TokenDTO();
        String token = jwtUtils.generateAccessTokenAdmin(admin);
        tokenDTO.setToken(token);

        return new ResponseDTO<>(0, "Login Sukses", tokenDTO);
    }

    @Override
    public ResponseDTO<Layanan> createProduct(String token, Layanan layanan) {
        if (token == null || token.isEmpty() || !jwtUtils.validateToken(token)) {
            return new ResponseDTO<>(108, "Token tidak valid atau kadaluwarsa", null);
        }

        String email = jwtUtils.extractEmail(token);
        Admin admin = adminRepository.findByEmail(email);

        if (admin == null) {
            return new ResponseDTO<>(108, "Token tidak valid atau kadaluwarsa", null);
        }

        Layanan product = layananRepository.save(layanan);
        return new ResponseDTO<>(0, "Product Created", product);
    }
}
