package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Admin;
import com.example.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Admin a WHERE a.email = :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT a FROM Admin a WHERE a.email = :email")
    Admin findByEmail(String email);
}
