package com.example.gastosTeste.service;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_wechat_qrcode.WeChatQRCode;
import org.bytedeco.opencv.opencv_core.StringVector;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class QRCodeReaderService {

    private final WeChatQRCode detector = new WeChatQRCode();

    public String readQRCode(MultipartFile file) throws IOException {
        Mat bytes = null;
        Mat imagem = null;
        StringVector resultados = null;

        try {
            bytes = new Mat(file.getBytes());
            imagem = opencv_imgcodecs.imdecode(bytes, opencv_imgcodecs.IMREAD_COLOR);

            if (imagem.empty()) {
                throw new IllegalArgumentException("Arquivo não é uma imagem válida");
            }

            resultados = detector.detectAndDecode(imagem);

            if (resultados.size() > 0) {
                return resultados.get(0).getString();
            }

            throw new IllegalArgumentException("QR Code não encontrado na imagem");
        } finally {
            if (resultados != null) resultados.close();
            if (imagem != null) imagem.close();
            if (bytes != null) bytes.close();
        }
    }
}