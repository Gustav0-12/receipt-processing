package com.example.gastosTeste.service;

import com.example.gastosTeste.dto.ReceiptDTO;
import com.example.gastosTeste.dto.ReceiptItemDTO;
import com.example.gastosTeste.dto.ReceiptMonthlyReportDTO;
import com.example.gastosTeste.dto.ReceiptReportDTO;
import com.example.gastosTeste.model.Receipt;
import com.example.gastosTeste.model.ReceiptItem;
import com.example.gastosTeste.repository.ReceiptRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReceiptService {

    private final ReceiptRepository repository;
    private final ReceiptPdfGenerator receiptPdfGenerator;

    public ReceiptService(ReceiptRepository repository, ReceiptPdfGenerator receiptPdfGenerator) {
        this.repository = repository;
        this.receiptPdfGenerator = receiptPdfGenerator;
    }

    public Receipt save(Receipt receipt) {
        if (receipt.getAcessKey() == null || receipt.getAcessKey().isBlank()) {
            throw new IllegalArgumentException("A chave de acesso é obrigatória");
        }

        if (repository.existsByAcessKey(receipt.getAcessKey())) {
            throw new IllegalArgumentException("Essa nota fiscal já foi registrada");
        }

        return repository.save(receipt);
    }

    public ReceiptMonthlyReportDTO getMonthlyReport(YearMonth period) {
        LocalDateTime startDate = period.atDay(1).atStartOfDay();
        LocalDateTime endDate = period.plusMonths(1).atDay(1).atStartOfDay();
        ReceiptMonthlyReportDTO report = repository.getMonthlyReport(startDate, endDate);

        if (report.receiptCount() == 0) {
            return new ReceiptMonthlyReportDTO(BigDecimal.ZERO, 0L, BigDecimal.ZERO, BigDecimal.ZERO, 0.0);
        }

        return report;
    }

    public byte[] generateMonthlyReportPdf(YearMonth period) {
        ReceiptMonthlyReportDTO receiptMonthly = getMonthlyReport(period);
        LocalDateTime startDate = period.atDay(1).atStartOfDay();
        LocalDateTime endDate = period.plusMonths(1).atDay(1).atStartOfDay();
        List<ReceiptReportDTO> receipts = repository.findAllByIssueDateRange(startDate, endDate);
        return receiptPdfGenerator.generate(receiptMonthly, period, receipts);
    }

    public List<ReceiptDTO> findAll() {
        return repository.findAll().stream().map(this::receiptToDto).toList();
    }

    public ReceiptDTO findById(Long id) {
        Receipt receipt = repository.findById(id).orElseThrow(() -> new RuntimeException("Receipt not found"));
        return receiptToDto(receipt);
    }

    public Receipt mapToReceipt(ReceiptDTO dto) {
        Receipt receipt = new Receipt();

        receipt.setAcessKey(dto.accessKey());
        receipt.setStoreName(dto.storeName());
        receipt.setCnpj(dto.cnpj());
        receipt.setIssueDate(dto.issueDate());
        receipt.setTotal(dto.total());

        for (ReceiptItemDTO itemDTO : dto.items()) {
            ReceiptItem item = mapToReceiptItem(itemDTO);
            receipt.addItem(item);
        }

        return receipt;
    }

    public ReceiptItem mapToReceiptItem(ReceiptItemDTO dto) {
        ReceiptItem item = new ReceiptItem();

        item.setProductName(dto.productName());
        item.setProductCode(dto.productCode());
        item.setQuantity(dto.quantity());
        item.setUnit(dto.unit());
        item.setUnitPrice(dto.unitPrice());
        item.setTotalPrice(dto.totalPrice());

        return item;
    }

    public ReceiptDTO receiptToDto(Receipt receipt) {
        return new ReceiptDTO(receipt.getId(),
                receipt.getAcessKey(), receipt.getStoreName(), receipt.getCnpj(), receipt.getIssueDate(), receipt.getTotal(), receipt.getItems().stream()
                        .map(item -> new ReceiptItemDTO(
                                item.getProductName(),
                                item.getProductCode(),
                                item.getQuantity(),
                                item.getUnit(),
                                item.getUnitPrice(),
                                item.getTotalPrice()
                        )).toList());
    }

}
