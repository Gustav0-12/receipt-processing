package com.example.gastosTeste.service;

import com.example.gastosTeste.dto.ReceiptDTO;
import com.example.gastosTeste.dto.ReceiptItemDTO;
import com.example.gastosTeste.model.Receipt;
import com.example.gastosTeste.model.ReceiptItem;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ReceiptImportService {

    private final ReceiptService receiptService;
    private final NfceConsultaService nfceConsultaService;
    private final NfceHtmlParser htmlParser;
    private final QRCodeReaderService qrCodeReaderService;

    public ReceiptImportService(ReceiptService receiptService, NfceConsultaService nfceConsultaService, NfceHtmlParser htmlParser, QRCodeReaderService qrCodeReaderService) {
        this.receiptService = receiptService;
        this.nfceConsultaService = nfceConsultaService;
        this.htmlParser = htmlParser;
        this.qrCodeReaderService = qrCodeReaderService;
    }

    public ReceiptDTO importReceipt(MultipartFile file) throws IOException {
        String url = qrCodeReaderService.readQRCode(file);
        Document document = nfceConsultaService.consultarHtml(url);
        ReceiptDTO receiptDTO = htmlParser.parse(document);

        Receipt receipt = receiptService.mapToReceipt(receiptDTO);
        Receipt savedReceipt = receiptService.save(receipt);

        return new ReceiptDTO(savedReceipt.getId(), receiptDTO.accessKey(), receiptDTO.storeName(), receiptDTO.cnpj(), receiptDTO.issueDate(), receiptDTO.total(), receiptDTO.items());
    }


}
