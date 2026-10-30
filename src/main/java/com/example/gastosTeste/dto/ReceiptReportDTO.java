package com.example.gastosTeste.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReceiptReportDTO(
        String storeName,
        String cnpj,
        LocalDateTime issueDate,
        BigDecimal total
) {}