package com.example.gastosTeste.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;


import java.io.IOException;

@Service
public class NfceConsultaService {

    public Document consultarHtml(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(15000)
                .get();

    }
}
