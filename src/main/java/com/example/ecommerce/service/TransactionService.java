package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.TopUpRequest;
import com.example.ecommerce.dto.request.TransactionHistoryWrapper;
import com.example.ecommerce.dto.request.TransactionRequest;
import com.example.ecommerce.dto.response.BalanceResponse;
import com.example.ecommerce.dto.response.ResponseDTO;
import com.example.ecommerce.dto.response.TransactionResponse;

public interface TransactionService {
    ResponseDTO<BalanceResponse> getBalance(String token);
    ResponseDTO<BalanceResponse> topUp(String token, TopUpRequest topUpRequest);
    ResponseDTO<TransactionResponse> createTransaction(String token, TransactionRequest transactionRequest);
    ResponseDTO<TransactionHistoryWrapper> getTransactionHistory(String token, Integer offSet , Integer limit);
}
