package com.sku.timetable.service;

import com.sku.timetable.dto.TimetableSlotResponseDto;

import java.util.List;

public interface TimetableService {

    /**
     * F-401 개인 시간표 조회(주간 뷰)
     */
    List<TimetableSlotResponseDto> getMyWeeklyTimetable(String studentNumber);
}
