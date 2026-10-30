package com.example.gastosTeste.controller;

import com.example.gastosTeste.dto.ReceiptDTO;
import com.example.gastosTeste.dto.ReceiptMonthlyReportDTO;
import com.example.gastosTeste.service.ReceiptImportService;
import com.example.gastosTeste.service.ReceiptPdfGenerator;
import com.example.gastosTeste.service.ReceiptService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {

    private final ReceiptImportService receiptImportService;
    private final ReceiptService receiptService;

    public ReceiptController(ReceiptImportService receiptImportService, ReceiptService receiptService) {
        this.receiptImportService = receiptImportService;
        this.receiptService = receiptService;
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReceiptDTO> importReceipt(@RequestParam("file") MultipartFile file) throws IOException {
        ReceiptDTO receipt = receiptImportService.importReceipt(file);
        return ResponseEntity.ok(receipt);
    }

    @GetMapping("/reports")
    public ResponseEntity<ReceiptMonthlyReportDTO> getMonthlyReport(@RequestParam int year, @RequestParam int month) {
        YearMonth period = YearMonth.of(year, month);
        ReceiptMonthlyReportDTO report = receiptService.getMonthlyReport(period);
        return ResponseEntity.ok(report);
    }

    @GetMapping(value = "/reports/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateMonthlyReportPdf(@RequestParam int year, @RequestParam int month) {
        YearMonth period = YearMonth.of(year, month);
        byte[] pdf = receiptService.generateMonthlyReportPdf(period);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename("receipt-monthly-report.pdf").build().toString())
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping
    public ResponseEntity<List<ReceiptDTO>> findAll() {
        List<ReceiptDTO> receipt = receiptService.findAll();
        return ResponseEntity.ok(receipt);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceiptDTO> findById(@PathVariable Long id) {
        ReceiptDTO receipt = receiptService.findById(id);
        return ResponseEntity.ok(receipt);
    }
}