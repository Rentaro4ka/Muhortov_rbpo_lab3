package com.example.lab3.repository;

import com.example.lab3.model.ItemClaim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemClaimRepository extends JpaRepository<ItemClaim, Long> {
}