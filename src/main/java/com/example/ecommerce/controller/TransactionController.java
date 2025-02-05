package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.TopUpRequest;
import com.example.ecommerce.dto.request.TransactionHistoryWrapper;
import com.example.ecommerce.dto.request.TransactionRequest;
import com.example.ecommerce.dto.response.BalanceResponse;
import com.example.ecommerce.dto.response.ResponseDTO;
import com.example.ecommerce.dto.response.TransactionResponse;
import com.example.ecommerce.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@Tag(name = "3. Module Transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/balance")
    public ResponseEntity<ResponseDTO<BalanceResponse>> getBalance(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(new ResponseDTO<>(108, "Token tidak valid atau kadaluwarsa", null));
        }
        String token = authHeader.replace("Bearer ", "");
        ResponseDTO<BalanceResponse> response = transactionService.getBalance(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/topup")
    public ResponseEntity<ResponseDTO<BalanceResponse>> topup(HttpServletRequest request, @RequestBody TopUpRequest topUpRequest) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(new ResponseDTO<>(108, "Token tidak valid atau kadaluwarsa", null));
        }
        String token = authHeader.replace("Bearer ", "");
        ResponseDTO<BalanceResponse>  response = transactionService.topUp(token, topUpRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transaction")
    public ResponseEntity<ResponseDTO<TransactionResponse>> createTransaction(HttpServletRequest request, @RequestBody TransactionRequest transactionRequest) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(new ResponseDTO<>(108, "Token tidak valid atau kadaluwarsa", null));
        }
        String token = authHeader.replace("Bearer ", "");
        ResponseDTO<TransactionResponse> response = transactionService.createTransaction(token, transactionRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transaction/history")
    public ResponseEntity<ResponseDTO<TransactionHistoryWrapper>> getTransactionHistory(
            HttpServletRequest request,
            @RequestParam(value = "offSet", required = false, defaultValue = "0") Integer offSet,
            @RequestParam(value = "limit", required = false, defaultValue = "3") Integer limit) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(new ResponseDTO<>(108, "Token tidak valid atau kadaluwarsa", null));
        }
        String token = authHeader.replace("Bearer ", "");
        ResponseDTO<TransactionHistoryWrapper> response = transactionService.getTransactionHistory(token, offSet, limit);
        return ResponseEntity.ok(response);
    }
}
