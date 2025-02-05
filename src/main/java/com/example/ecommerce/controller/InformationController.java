package com.example.ecommerce.controller;

import com.example.ecommerce.dto.response.BannerResponse;
import com.example.ecommerce.dto.response.LayananResponse;
import com.example.ecommerce.dto.response.ResponseDTO;
import com.example.ecommerce.service.InformationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@Tag(name = "2. Module Information")
public class InformationController {

    @Autowired
    private InformationService informationService;

    @Operation(summary = "banner", security = {})
    @GetMapping("/banner")
    public ResponseEntity<ResponseDTO<List<BannerResponse>>> banner(){
        ResponseDTO<List<BannerResponse>> result = informationService.bannerAll();
        return ResponseEntity.status(result.getStatus() == 0 ? 200 : 400).body(result);
    }

    @GetMapping("/service")
    public ResponseEntity<ResponseDTO<List<LayananResponse>>> service(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(new ResponseDTO<>(108, "Token tidak valid atau kadaluwarsa", null));
        }

        String token = authHeader.replace("Bearer ", "");
        ResponseDTO<List<LayananResponse>> result = informationService.serviceAll(token);
        return ResponseEntity.ok(result);
    }
}
