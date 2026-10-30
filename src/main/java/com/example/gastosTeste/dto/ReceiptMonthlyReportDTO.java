package com.example.gastosTeste.dto;

import java.math.BigDecimal;

public record ReceiptMonthlyReportDTO(
        BigDecimal totalSpent,
        Long receiptCount,
        BigDecimal highestPurchase,
        BigDecimal lowestPurchase,
        Double averagePurchase
) {}