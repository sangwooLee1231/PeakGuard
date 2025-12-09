package com.sku.common.dto;

import com.sku.common.util.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * API 에러 응답 DTO
 * 컨트롤러에서 예외가 발생했을 때 클라이언트로 내려주는 공통 형식
 */
@Getter
@Builder
public class ErrorResponse {

    private final LocalDateTime timestamp; // 에러 발생 시각
    private final int status;              // HTTP 상태 코드 (ErrorCode.status)
    private final String code;             // 시스템 에러 코드 (예: AU001)
    private final String message;          // 사용자에게 보여줄 에러 메시지
    private final String path;             // 요청 URL (예: /api/lectures/1)

    /**
     * ErrorCode 를 그대로 사용하는 기본 팩토리 메서드
     */
    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMsg())
                .path(path)
                .build();
    }

    /**
     * ErrorCode의 기본 msg 대신, 상세 메시지를 덮어쓰고 싶을 때 사용
     */
    public static ErrorResponse of(ErrorCode errorCode, String path, String overrideMessage) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(overrideMessage)
                .path(path)
                .build();
    }
}
