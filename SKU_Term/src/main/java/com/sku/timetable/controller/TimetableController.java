package com.sku.timetable.controller;

import com.sku.common.dto.ResponseDto;
import com.sku.timetable.dto.TimetableSlotResponseDto;
import com.sku.timetable.service.TimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/timetable")
@RequiredArgsConstructor
public class TimetableController {

    private final TimetableService timetableService;

    /**
     * F-401 개인 시간표 조회 (주간 뷰)
     */
    @GetMapping("/my")
    public ResponseEntity<ResponseDto<Map<String, Object>>> getMyWeeklyTimetable(
            @AuthenticationPrincipal User user
    ) {
        String studentNumber = user.getUsername();

        List<TimetableSlotResponseDto> timetable =
                timetableService.getMyWeeklyTimetable(studentNumber);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "나의 주간 시간표 조회 성공",
                        Map.of("timetable", timetable)
                )
        );
    }
}
