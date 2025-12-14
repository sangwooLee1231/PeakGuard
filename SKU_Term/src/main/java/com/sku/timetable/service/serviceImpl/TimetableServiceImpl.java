package com.sku.timetable.service.serviceImpl;

import com.sku.common.exception.CustomException;
import com.sku.common.util.ErrorCode;
import com.sku.member.mapper.StudentMapper;
import com.sku.member.vo.Student;
import com.sku.timetable.dto.TimetableSlotResponseDto;
import com.sku.timetable.mapper.TimetableMapper;
import com.sku.timetable.service.TimetableService;
import com.sku.timetable.vo.TimetableSlot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimetableServiceImpl implements TimetableService {

    private final TimetableMapper timetableMapper;
    private final StudentMapper studentMapper;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    @Transactional(readOnly = true)
    public List<TimetableSlotResponseDto> getMyWeeklyTimetable(String studentNumber) {

        // 1. 학생 조회
        Student student = studentMapper.findByStudentNumber(studentNumber);
        if (student == null) {
            throw new CustomException(ErrorCode.STUDENT_NOT_FOUND);
        }
        Long studentId = student.getId();

        // 2. 시간표 데이터 조회
        List<TimetableSlot> slots;
        try {
            slots = timetableMapper.findWeeklyTimetable(studentId);
        } catch (Exception e) {
            log.error("개인 시간표 조회 중 오류 발생. studentId={}", studentId, e);
            throw new CustomException(ErrorCode.TIMETABLE_LOAD_FAILED, e);
        }

        // 3. VO -> DTO 변환
        List<TimetableSlotResponseDto> result = new ArrayList<>();
        for (TimetableSlot slot : slots) {
            TimetableSlotResponseDto dto = new TimetableSlotResponseDto();
            dto.setLectureId(slot.getLectureId());
            dto.setCourseName(slot.getCourseName());
            dto.setProfessor(slot.getProfessor());
            dto.setCredit(slot.getCredit());
            dto.setDivision(slot.getDivision());
            dto.setRoom(slot.getRoom());
            dto.setDayOfWeek(slot.getDayOfWeek());
            dto.setStartTime(formatTime(slot.getStartTime()));
            dto.setEndTime(formatTime(slot.getEndTime()));
            dto.setPeriod(calculatePeriod(slot.getStartTime())); // 9~18 기준 교시 계산

            result.add(dto);
        }
        return result;
    }

    private String formatTime(LocalTime time) {
        return time != null ? time.format(TIME_FORMATTER) : null;
    }

    private Integer calculatePeriod(LocalTime startTime) {
        if (startTime == null) {
            return null;
        }
        int hour = startTime.getHour(); // 항상 정각이라고 가정 (09, 10, 11 ...)
        int baseHour = 9;               // 1교시 시작 시각
        int period = hour - baseHour + 1;
        return period > 0 ? period : null;
    }
}
