package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.response.BannerResponse;
import com.example.ecommerce.dto.response.ResponseDTO;
import com.example.ecommerce.dto.response.LayananResponse;
import com.example.ecommerce.entity.Banner;
import com.example.ecommerce.entity.Layanan;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.BannerRepository;
import com.example.ecommerce.repository.LayananRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.security.JwtUtils;
import com.example.ecommerce.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InformationServiceImpl implements InformationService {

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private LayananRepository layananRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public ResponseDTO<List<BannerResponse>> bannerAll() {
        List<Banner> banners = bannerRepository.findAll();
        List<BannerResponse> bannerResponseList = new ArrayList<>();
        for (Banner banner : banners){
            BannerResponse bannerResponse = new BannerResponse();
            bannerResponse.setBannerName(banner.getBannerName());
            bannerResponse.setBannerImage(banner.getBannerImage());
            bannerResponse.setDescription(banner.getDescription());
            bannerResponseList.add(bannerResponse);
        }
        return new ResponseDTO<>(0, "Sukses", bannerResponseList);
    }

    @Override
    public ResponseDTO<List<LayananResponse>> serviceAll(String token) {
        if (token == null || token.isEmpty() || !jwtUtils.validateToken(token)) {
            return new ResponseDTO<>(108, "Token tidak valid atau kadaluwarsa", null);
        }
        String email = jwtUtils.extractEmail(token);
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return new ResponseDTO<>(108, "Token tidak valid atau kadaluwarsa", null);
        }

        List<Layanan> allLayanan = layananRepository.findAll();
        List<LayananResponse> layananResponseList = new ArrayList<>();

        for (Layanan layanan : allLayanan){
            LayananResponse layananResponse = new LayananResponse();
            layananResponse.setServiceCode(layanan.getServiceCode());
            layananResponse.setServiceName(layanan.getServiceName());
            layananResponse.setServiceIcon(layanan.getServiceIcon());
            layananResponse.setServiceTariff(layanan.getServiceTariff());
            layananResponseList.add(layananResponse);
        }
        return new ResponseDTO<>(0, "Sukses", layananResponseList);
    }
}
