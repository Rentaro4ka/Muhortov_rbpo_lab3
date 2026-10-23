package com.example.lab3.repository;

import com.example.lab3.model.HandoverRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HandoverRecordRepository extends JpaRepository<HandoverRecord, Long> {
}