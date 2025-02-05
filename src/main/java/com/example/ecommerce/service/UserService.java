package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.LoginUserRequest;
import com.example.ecommerce.dto.request.ProfileUpdateRequest;
import com.example.ecommerce.dto.response.ProfileResponse;
import com.example.ecommerce.dto.response.ResponseDTO;
import com.example.ecommerce.dto.response.TokenDTO;
import com.example.ecommerce.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    ResponseDTO<Void> registration(User user);
    ResponseDTO<TokenDTO> login(LoginUserRequest request);
    ResponseDTO<ProfileResponse> getProfile(String token);
    ResponseDTO<ProfileResponse> updateProfile(String token, ProfileUpdateRequest updateRequestDTO);
    ResponseDTO<ProfileResponse> uploadProfileImage(String token, MultipartFile file);
}
