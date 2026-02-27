package com.example.ai.rag;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Log4j2
@RestController
@Tag(name = "OpenAI LLM", description = "OpenAI LLM 테스트 - RAG")
public class RagController {

    private final RagService ragService;
    private final ChatClient chatClient;

    // 화면단 enctype = "multipart/form-data" : frontend using 'Multipart'
    // MultipartFile = uploaded file received in a multiple requests.
    // consumes = MediaType.Multipart_from_DATA_VALUE 선택
    // Checking validation Priority in Frontend first, Backend then.
    @PostMapping(value = "/document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDTO<String>> handleFileUpload(@RequestParam("file") MultipartFile file) {
        log.info("file upload {}", file.getOriginalFilename());

        // 필수
        if (file.isEmpty()) {
            log.warn("빈 파일 ");
            return ResponseEntity.badRequest().body(ApiResponseDTO.failure("파일이 비어있습니다"));
        }

        // 선택
        if (!file.getOriginalFilename().endsWith(".pdf") && !file.getOriginalFilename().endsWith(".PDF")) {
            log.warn("지원하지 않는 파일 형식");
            return ResponseEntity.badRequest().body(ApiResponseDTO.failure("PDF file upload Only"));
        }

        // service 호출

        File tempFile = null;

        // Save Temp File
        try {
            // 파일 저장
            tempFile = File.createTempFile("upload_", ".pdf");
            log.debug("임시 파일 저장 {}", tempFile.getAbsolutePath());
            file.transferTo(tempFile);
        } catch (Exception e) {
            log.warn("임시 파일 저장 중 에러");
            return ResponseEntity.badRequest().body(ApiResponseDTO.failure("Error with Saving File"));
        }

        // Delete Temp File
        try {
            String docId = ragService.uploadPdfFile(tempFile, file.getOriginalFilename());
            log.info("Success Uploading File", docId);
            return ResponseEntity.ok(ApiResponseDTO.success(docId));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 임시 파일 제거 (Delete Temp Files)
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDTO.failure("알 수 없는 에러 발생"));
    }

    @PostMapping("/api/v1/rag")
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> postRag(@RequestBody QueryRequestDTO requestDTO,
            @RequestHeader(value = "X-CHAT-ID", required = false) String chatId) {
        log.info("질의요청", requestDTO);

        if (requestDTO.getQuery().isBlank()) {
            log.info("빈 질의 요청");
            return ResponseEntity.badRequest().body(ApiResponseDTO.failure("질의가 비어있습니다"));
        }

        String conversationId = (chatId == null || chatId.isBlank()) ? UUID.randomUUID().toString() : chatId;

        List<DocumentSearchResultDTO> resultDTOs = ragService.retrieve(requestDTO.getQuery(),
                requestDTO.getMaxResult());
        String answer = ragService.generateAnswerWithContexts(requestDTO.getQuery(), resultDTOs, conversationId);

        return ResponseEntity.ok(ApiResponseDTO.success(Map.of("conversationId", conversationId, "answer", answer)));
    }

}
