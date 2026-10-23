package com.example.lab3.repository;

import com.example.lab3.model.LossReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LossReportRepository extends JpaRepository<LossReport, Long> {
}