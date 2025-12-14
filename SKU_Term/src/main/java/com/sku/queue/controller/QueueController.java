package com.sku.queue.controller;

import com.sku.common.dto.ResponseDto;
import com.sku.queue.dto.QueueJoinResponseDto;
import com.sku.queue.dto.QueueStatusResponseDto;
import com.sku.queue.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/queue")
@RequiredArgsConstructor
public class QueueController {

    private final QueueService queueService;

    @PostMapping("/join")
    public ResponseEntity<ResponseDto<Map<String, Object>>> joinQueue() {

        QueueJoinResponseDto result = queueService.joinQueue();

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "대기열에 진입했습니다.",
                        Map.of(
                                "queueToken", result.getQueueToken(),
                                "queueNumber", result.getQueueNumber(),
                                "position", result.getPosition()
                        )
                )
        );
    }

    /**
     * 대기열 상태 조회
     */
    @GetMapping("/status")
    public ResponseEntity<ResponseDto<Map<String, Object>>> getStatus(
            @RequestParam("token") String queueToken
    ) {
        QueueStatusResponseDto status = queueService.getStatus(queueToken);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "대기열 상태 조회 성공",
                        Map.of("queueStatus", status)
                )
        );
    }
}
