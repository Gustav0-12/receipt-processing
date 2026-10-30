package com.example.gastosTeste.service;

import com.example.gastosTeste.dto.ReceiptMonthlyReportDTO;
import com.example.gastosTeste.dto.ReceiptReportDTO;
import com.example.gastosTeste.model.Receipt;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Component
public class ReceiptPdfGenerator {

    private final TemplateEngine templateEngine;

    public ReceiptPdfGenerator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generate(ReceiptMonthlyReportDTO report, YearMonth period, List<ReceiptReportDTO> receipts) {
        String periodLabel = period.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt-BR")) + " de " + period.getYear();
        Context ctx = new Context(Locale.forLanguageTag("pt-BR"));
        ctx.setVariable("report", report);
        ctx.setVariable("receipts", receipts);
        ctx.setVariable("periodLabel", periodLabel);

        String html = templateEngine.process("receipt-monthly-report", ctx);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(out);
            builder.run();
            return out.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao gerar o PDF do relatório.", e);
        }
    }
}
