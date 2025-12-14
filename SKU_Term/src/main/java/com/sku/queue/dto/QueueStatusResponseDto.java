package com.sku.queue.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 대기열 상태 조회 응답 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueueStatusResponseDto {

    private String queueToken;          // 토큰
    private Long queueNumber;           // 발급된 대기 번호
    private Long position;              // 현재 대기열 내 위치
    private boolean active;             // 현재 수강신청 가능 상태인지 여부
    private Long estimatedWaitSeconds;  // 예상 대기 시간(대략 값)
}
