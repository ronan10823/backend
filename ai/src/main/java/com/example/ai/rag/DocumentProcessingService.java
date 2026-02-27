package com.example.ai.rag;

import java.io.File;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class DocumentProcessingService {

    public String extractTextFormPdf(File pdfFile) {
        log.info("pdf 텍스트 추출 {}", pdfFile);

        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile(pdfFile))) {
            log.debug("pdf 문서 로드 성공 {} 페이지", document.getNumberOfPages());
            return new PDFTextStripper().getText(document);

        } catch (Exception e) {
            log.error("텍스트 추출 실패");
            throw new DocumentProcessingException(HttpStatus.BAD_REQUEST, "pdf text 추출 실패" + e.getMessage(), e);
        }
    }
}
