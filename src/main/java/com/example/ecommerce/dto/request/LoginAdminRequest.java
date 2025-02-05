package com.example.ecommerce.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginAdminRequest {
    private String email;
    private String password;
}
