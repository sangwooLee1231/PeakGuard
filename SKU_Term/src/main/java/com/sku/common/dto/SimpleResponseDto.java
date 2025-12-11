package com.sku.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * HTTP 응답에 상태 코드와 메시지만 담는 DTO
 */
@Data
@AllArgsConstructor
public class SimpleResponseDto {

    private int status;     // HTTP 상태 코드
    private String message; // 응답 메시지
}
