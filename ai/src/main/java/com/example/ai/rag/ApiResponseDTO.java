package com.example.ai.rag;

// 응답 객체 - 성공과 실패 시 둘 다 사용
// Response Object - Use in Success & Failure
public record ApiResponseDTO<T>(boolean success, T data, String errorMsg) {

    public static <T> ApiResponseDTO<T> success(T data) {
        return new ApiResponseDTO<>(true, data, null);
    }

    public static <T> ApiResponseDTO<T> failure(String errorMsg) {
        return new ApiResponseDTO<>(false, null, errorMsg);
    }
}
