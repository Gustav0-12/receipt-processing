package com.example.gastosTeste.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ReceiptDTO(
        Long id,
        String accessKey,
        String storeName,
        String cnpj,
        LocalDateTime issueDate,
        BigDecimal total,
        List<ReceiptItemDTO> items
) {}