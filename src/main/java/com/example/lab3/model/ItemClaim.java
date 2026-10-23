package com.example.lab3.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item_claims")
public class ItemClaim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loss_report_id", nullable = false)
    private Long lossReportId;

    @Column(name = "found_item_id", nullable = false)
    private Long foundItemId;

    private String evidence;

    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}