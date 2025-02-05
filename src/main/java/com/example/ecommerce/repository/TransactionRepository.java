package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId ORDER BY t.createdOn DESC")
    List<Transaction> findByUserIdOrderByCreatedOnDesc(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId ORDER BY t.createdOn DESC")
    List<Transaction> findByUserIdOrderByCreatedOnDesc(@Param("userId") Long userId);
}
