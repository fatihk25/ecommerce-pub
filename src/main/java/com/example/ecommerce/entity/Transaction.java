package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;
    private String invoiceNumber;
    private String transactionType;
    private String currency;
    private String deviceId;
    private String location;
    private String description;
    private Integer totalAmount;
    private String status;
    private Boolean fraudCheck;
    private LocalDateTime createdOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
