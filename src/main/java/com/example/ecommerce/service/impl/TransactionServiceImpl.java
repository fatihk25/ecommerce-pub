package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.request.TopUpRequest;
import com.example.ecommerce.dto.request.TransactionHistoryWrapper;
import com.example.ecommerce.dto.request.TransactionRequest;
import com.example.ecommerce.dto.response.BalanceResponse;
import com.example.ecommerce.dto.response.ResponseDTO;
import com.example.ecommerce.dto.response.TransactionHistoryResponse;
import com.example.ecommerce.dto.response.TransactionResponse;
import com.example.ecommerce.entity.Balance;
import com.example.ecommerce.entity.Layanan;
import com.example.ecommerce.entity.Transaction;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.BalanceRepository;
import com.example.ecommerce.repository.LayananRepository;
import com.example.ecommerce.repository.TransactionRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.security.JwtUtils;
import com.example.ecommerce.service.TransactionService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private LayananRepository layananRepository;

    @Override
    public ResponseDTO<BalanceResponse> getBalance(String token) {
        if (token == null || token.isEmpty() || !jwtUtils.validateToken(token)) {
            return new ResponseDTO<>(108, "Token tidak valid atau kadaluwarsa", null);
        }
        String email = jwtUtils.extractEmail(token);
        User user = userRepository.findByEmail(email);
        Balance balance = balanceRepository.findByUserId(user.getId());

        BalanceResponse newBalance = new BalanceResponse();
        newBalance.setBalance(balance.getBalance());
        return new ResponseDTO<>(0, "Get Balance Berhasil", newBalance);
    }

    @Override
    public ResponseDTO<BalanceResponse> topUp(String token, TopUpRequest topUpRequest) {
        if (token == null || token.isEmpty() || !jwtUtils.validateToken(token)) {
            return new ResponseDTO<>(108, "Token tidak valid atau kadaluwarsa", null);
        }

        if (topUpRequest.getTopUpAmount() == null || topUpRequest.getTopUpAmount() <= 0) {
            return new ResponseDTO<>(102, "Parameter amount hanya boleh angka dan tidak boleh lebih kecil dari 0", null);
        }
        String email = jwtUtils.extractEmail(token);
        User user = userRepository.findByEmail(email);

        Balance balance = balanceRepository.findByUserId(user.getId());

        if (balance == null || ObjectUtils.isEmpty(balance) ) {
            balance = new Balance();
            balance.setUser(user);
            balance.setBalance(topUpRequest.getTopUpAmount());
        } else {
            balance.setBalance(balance.getBalance() + (topUpRequest.getTopUpAmount()));
        }
        balanceRepository.save(balance);

        // Create and save the transaction
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setTransactionType("TOPUP");

        transaction.setTotalAmount(topUpRequest.getTopUpAmount());
        transaction.setInvoiceNumber(LocalDateTime.now() + ".000Z");
        transaction.setCreatedOn(LocalDateTime.now());
        transaction.setDescription("Top Up Balance");
        transactionRepository.save(transaction);

        BalanceResponse balanceResponse = new BalanceResponse();
        balanceResponse.setBalance(balance.getBalance());

        return new ResponseDTO<>(0, "Top Up Balance berhasil", balanceResponse);
    }

    @Override
    public ResponseDTO<TransactionResponse> createTransaction(String token, TransactionRequest transactionRequest) {
        if (token == null || token.isEmpty() || !jwtUtils.validateToken(token)) {
            return new ResponseDTO<>(108, "Token tidak valid atau kadaluwarsa", null);
        }
        String email = jwtUtils.extractEmail(token);
        User user = userRepository.findByEmail(email);

        Layanan layanan = layananRepository.findByServiceCode(transactionRequest.getServiceCode());
        if (layanan == null) {
            return new ResponseDTO<>(102, "Service atau Layanan tidak ditemukan", null);
        }

        Balance balance = balanceRepository.findByUserId(user.getId());
        if (balance.getBalance().compareTo(layanan.getServiceTariff()) < 0) {
            return new ResponseDTO<>(103, "Saldo tidak mencukupi", null);
        }

        balance.setBalance(balance.getBalance() - layanan.getServiceTariff());
        balanceRepository.save(balance);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setTransactionType("PAYMENT");
        transaction.setTotalAmount(layanan.getServiceTariff());
        transaction.setInvoiceNumber(LocalDateTime.now() + ".000Z");
        transaction.setCreatedOn(LocalDateTime.now());
        transaction.setDescription("Payment for " + layanan.getServiceName());
        transactionRepository.save(transaction);

        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setInvoiceNumber(transaction.getInvoiceNumber());
        transactionResponse.setServiceCode(layanan.getServiceCode());
        transactionResponse.setServiceName(layanan.getServiceName());
        transactionResponse.setTransactionType("PAYMENT");
        transactionResponse.setTotalAmount(layanan.getServiceTariff());
        transactionResponse.setCreatedOn(transaction.getCreatedOn());

        return new ResponseDTO<>(0, "Transaksi berhasil", transactionResponse);
    }

    @Override
    public ResponseDTO<TransactionHistoryWrapper> getTransactionHistory(String token, Integer offSet ,Integer limit){
        String email = jwtUtils.extractEmail(token);
        User user = userRepository.findByEmail(email);
        if (token == null || token.isEmpty() || !jwtUtils.validateToken(token)) {
            return new ResponseDTO<>(108, "Token tidak valid atau kadaluwarsa", null);
        }

        List<Transaction> transactions;
        if (limit != null && limit > 0) {
            transactions = transactionRepository.findByUserIdOrderByCreatedOnDesc(user.getId(), PageRequest.of(offSet, limit));
        } else {
            transactions = transactionRepository.findByUserIdOrderByCreatedOnDesc(user.getId());
        }

        List<TransactionHistoryResponse> transactionHistoryList = transactions.stream()
                .map(t -> TransactionHistoryResponse.builder()
                        .invoiceNumber(t.getInvoiceNumber())
                        .transactionType(t.getTransactionType())
                        .description(t.getDescription())
                        .totalAmount(t.getTotalAmount())
                        .createdOn(t.getCreatedOn())
                        .build())
                .toList();

        TransactionHistoryWrapper historyWrapperDTO = TransactionHistoryWrapper.builder()
                .offset(offSet)
                .limit(limit != null ? limit : transactionHistoryList.size())
                .records(transactionHistoryList)
                .build();

        return new ResponseDTO<>(0, "Get History Berhasil", historyWrapperDTO);
    }
}
