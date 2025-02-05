package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.request.LoginUserRequest;
import com.example.ecommerce.dto.request.ProfileUpdateRequest;
import com.example.ecommerce.dto.response.ProfileResponse;
import com.example.ecommerce.dto.response.ResponseDTO;
import com.example.ecommerce.dto.response.TokenDTO;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.security.JwtUtils;
import com.example.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public ResponseDTO<Void> registration(User user) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!user.getEmail().matches(emailRegex)) {
            return new ResponseDTO<>(102, "Parameter email tidak sesuai format", null);
        }

        // Validasi password minimal 8 karakter
        if (user.getPassword().length() < 8) {
            return new ResponseDTO<>(103, "Password harus memiliki panjang minimal 8 karakter", null);
        }

        // Cek email sudah terdaftar
        if (userRepository.existsByEmail(user.getEmail())) {
            return new ResponseDTO<>(104, "Email sudah terdaftar", null);
        }

        // Hash password dan simpan user
        String hashedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);

        return new ResponseDTO<>(0, "Registrasi berhasil silahkan login", null);
    }

    @Override
    public ResponseDTO<TokenDTO> login(LoginUserRequest request){
        User user = userRepository.findByEmail(request.getEmail());

        // Validasi Format Email
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!request.getEmail().matches(emailRegex)) {
            return new ResponseDTO<>(102, "Parameter email tidak sesuai format", null);
        }

        // Validasi Email
        if (user == null) {
            return new ResponseDTO<>(102, "Username atau password salah", null);
        }

        // Validasi Password
        Boolean isPasswordMatch = new BCryptPasswordEncoder().matches(request.getPassword(), user.getPassword());
        if (!new BCryptPasswordEncoder().matches(request.getPassword(), user.getPassword())) {
            return new ResponseDTO<>(103, "Username atau password salah", null);
        }

        // Set Token
        TokenDTO tokenDTO = new TokenDTO();
        String token = jwtUtils.generateAccessTokenUser(user);
        tokenDTO.setToken(token);

        return new ResponseDTO<>(0, "Login Sukses", tokenDTO);
    }

    @Override
    public ResponseDTO<ProfileResponse> getProfile(String token){
        if (token == null || token.isEmpty() || !jwtUtils.validateToken(token)) {
            return new ResponseDTO<>(108, "Token tidak valid atau kadaluwarsa", null);
        }
        String email = jwtUtils.extractEmail(token);
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return new ResponseDTO<>(108, "Token tidak valid atau kadaluwarsa", null);
        }

        ProfileResponse profileResponse = new ProfileResponse(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getProfileImage()
        );

        return new ResponseDTO<>(0, "Sukses", profileResponse);
    }

    @Override
    public ResponseDTO<ProfileResponse> updateProfile(String token, ProfileUpdateRequest updateRequestDTO){
        if (!jwtUtils.validateToken(token)){
            return new ResponseDTO<>(108, "Token tidak valid atau kadaluwarsa", null);
        }
        String email = jwtUtils.extractEmail(token);
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return new ResponseDTO<>(108, "User tidak ditemukan", null);
        }

        Optional.ofNullable(updateRequestDTO.getFirstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(updateRequestDTO.getLastName()).ifPresent(user::setLastName);

        userRepository.saveAndFlush(user);

        return new ResponseDTO<>(0, "Update Profile berhasil", new ProfileResponse(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getProfileImage()
        ));
    }

    @Override
    public ResponseDTO<ProfileResponse> uploadProfileImage(String token, MultipartFile file){
        long MAX_FILE_SIZE = 10 * 1024 * 1024;

        if (!jwtUtils.validateToken(token)) {
            return new ResponseDTO<>(108, "Token tidak valid atau kadaluwarsa", null);
        }

        String email = jwtUtils.extractEmail(token);
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return new ResponseDTO<>(108, "Token tidak valid atau kadaluwarsa", null);
        }

        String contentType = file.getContentType();
        if (contentType.isEmpty() || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            return new ResponseDTO<>(102, "Format Image tidak sesuai", null);
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return new ResponseDTO<>(102, "Format Image tidak sesuai", null);
        }

        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String imageUrl = "https://yoururlapi.com/" + fileName;
            user.setProfileImage(imageUrl);
            userRepository.saveAndFlush(user);
        } catch (Exception e){
            return new ResponseDTO<>(102, "Format Image tidak sesuai", null);
        }
        return new ResponseDTO<>(0, "Update Profile Image berhasil", new ProfileResponse(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getProfileImage()
        ));
    }
}
