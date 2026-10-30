package com.example.gastosTeste.dto;

import java.math.BigDecimal;

public record ReceiptItemDTO(
        String productName,
        String productCode,
        BigDecimal quantity,
        String unit,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) {}