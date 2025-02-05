package com.example.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uuid;

    @Column(unique = true)
    private String email;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String phone;

    private String country;

    private String password;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @JsonProperty("profile_image")
    private String profileImage;
}
