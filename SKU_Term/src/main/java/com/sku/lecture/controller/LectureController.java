package com.sku.lecture.controller;

import com.sku.common.dto.ResponseDto;
import com.sku.lecture.dto.LectureDetailResponseDto;
import com.sku.lecture.dto.LectureListResponseDto;
import com.sku.lecture.dto.LectureSearchCondition;
import com.sku.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    /**
     * 강의 목록 조회
     * 강의 검색
     * 필터링
     */
    @GetMapping
    public ResponseEntity<ResponseDto<Map<String, Object>>> getLectures(
            @ModelAttribute LectureSearchCondition condition
    ) {
        List<LectureListResponseDto> lectures = lectureService.getLectures(condition);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "강의 목록 조회 성공",
                        Map.of("lectures", lectures)
                )
        );
    }

    @GetMapping("/{lectureId}")
    public ResponseEntity<ResponseDto<Map<String, Object>>> getLectureDetail(
            @PathVariable Long lectureId
    ) {
        LectureDetailResponseDto detail = lectureService.getLectureDetail(lectureId);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "강의 상세 조회 성공",
                        Map.of("lecture", detail)
                )
        );
    }
}
