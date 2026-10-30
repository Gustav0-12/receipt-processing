package com.example.gastosTeste.service;

import com.example.gastosTeste.dto.ReceiptItemDTO;
import com.example.gastosTeste.dto.ReceiptDTO;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class NfceHtmlParser {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ssXXX");

    public ReceiptDTO parse(Document document) {
        String acessKey = document.select(".chave").text().replace(" ", "");
        String storeName = document.select("#u20").text();
        String cnpj = document.select(".txtCenter .text").first().text().replace("CNPJ:", "").trim();

        String dateText = document.select("#infos").text();
        String date = dateText.split("Série: ")[1].split("Emissão: ")[1].split(" - Via")[0];
        OffsetDateTime issueDate = OffsetDateTime.parse(date, DATE_FORMATTER);

        String totalText = document.select("#totalNota .txtMax").text();
        BigDecimal total = parseMoney(totalText);

        List<ReceiptItemDTO> products = new ArrayList<>();

        Elements items = document.select("#tabResult tr");
        for (Element item : items) {
            String productName = item.select(".txtTit").first().text();
            String productCode = item.select(".RCod").text().replace("(Código:", "").replace(")", "").trim();
            String quantityText = item.select(".Rqtd").text().replace("Qtde.:", "").trim();
            String unit = item.select(".RUN").text().replace("UN:", "").trim();
            String unitPriceText = item.select(".RvlUnit").text().replace("Vl. Unit.:", "").trim();
            String itemTotalText = item.select(".valor").text();

            ReceiptItemDTO receiptItemDTO = new ReceiptItemDTO(productName, productCode, parseDecimal(quantityText), unit, parseMoney(unitPriceText), parseMoney(itemTotalText));
            products.add(receiptItemDTO);
        }

        return new ReceiptDTO(null, acessKey, storeName, cnpj, issueDate.toLocalDateTime(), total, products);
    }

    private BigDecimal parseMoney(String value) {
        return new BigDecimal(
                value.replace(".", "").replace(",", ".")
        );
    }

    private BigDecimal parseDecimal(String value) {
        return new BigDecimal(value.replace(",", "."));
    }
}

