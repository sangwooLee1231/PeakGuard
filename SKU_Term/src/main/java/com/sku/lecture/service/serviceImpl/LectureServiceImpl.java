package com.sku.lecture.service.serviceImpl;

import com.sku.common.exception.CustomException;
import com.sku.common.util.ErrorCode;
import com.sku.lecture.dto.LectureDetailResponseDto;
import com.sku.lecture.dto.LectureListResponseDto;
import com.sku.lecture.dto.LectureSearchCondition;
import com.sku.lecture.dto.LectureTimeResponseDto;
import com.sku.lecture.mapper.LectureMapper;
import com.sku.lecture.service.LectureService;
import com.sku.lecture.vo.Lecture;
import com.sku.lecture.vo.LectureTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureMapper lectureMapper;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final Set<String> VALID_DAYS = Set.of("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN");

    @Override
    public List<LectureListResponseDto> getLectures(LectureSearchCondition condition) {

        validateSearchCondition(condition);

        List<Lecture> lectures;
        try {
            lectures = lectureMapper.findLectures(condition);
        } catch (Exception e) {
            log.error("강의 목록 조회 중 오류 발생", e);
            throw new CustomException(ErrorCode.LECTURE_SEARCH_FAILED, e);
        }

        List<LectureListResponseDto> result = new ArrayList<>();
        for (Lecture lecture : lectures) {
            result.add(convertToLectureListDto(lecture));
        }

        return result;
    }

    @Override
    public LectureDetailResponseDto getLectureDetail(Long lectureId) {

        if (lectureId == null || lectureId <= 0) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        Lecture lecture;
        try {
            lecture = lectureMapper.findById(lectureId);
        } catch (Exception e) {
            log.error("강의 단건 조회 중 오류 발생. lectureId={}", lectureId, e);
            throw new CustomException(ErrorCode.LECTURE_SEARCH_FAILED, e);
        }

        if (lecture == null) {
            throw new CustomException(ErrorCode.LECTURE_NOT_FOUND);
        }

        List<LectureTime> times;
        try {
            times = lectureMapper.findTimesByLectureId(lectureId);
        } catch (Exception e) {
            log.error("강의 시간 조회 중 오류 발생. lectureId={}", lectureId, e);
            throw new CustomException(ErrorCode.TIMETABLE_LOAD_FAILED, e);
        }

        List<LectureTimeResponseDto> timeDtos = new ArrayList<>();
        for (LectureTime time : times) {
            timeDtos.add(convertToLectureTimeDto(time));
        }

        LectureDetailResponseDto detail = new LectureDetailResponseDto();
        detail.setId(lecture.getId());
        detail.setCourseName(lecture.getCourseName());
        detail.setProfessor(lecture.getProfessor());
        detail.setCredit(lecture.getCredit());
        detail.setMaxCapacity(lecture.getMaxCapacity());
        detail.setCurrentCount(lecture.getCurrentCount());
        detail.setDivision(lecture.getDivision());
        detail.setRoom(lecture.getRoom());
        detail.setTimes(timeDtos);

        return detail;
    }

    /**
     * 검색/필터 조건 검증
     */
    private void validateSearchCondition(LectureSearchCondition condition) {

        if (condition == null) {
            return;
        }

        // 요일 값 검증
        if (condition.getDayOfWeek() != null && !condition.getDayOfWeek().isBlank()) {
            String day = condition.getDayOfWeek().toUpperCase(Locale.ROOT);
            if (!VALID_DAYS.contains(day)) {
                throw new CustomException(ErrorCode.LECTURE_INVALID_FILTER);
            }
            condition.setDayOfWeek(day); // 대문자로 정규화
        }

        // 시간 범위 검증
        if (condition.getStartTime() != null && condition.getEndTime() != null) {
            if (condition.getStartTime().isAfter(condition.getEndTime())) {
                throw new CustomException(ErrorCode.LECTURE_INVALID_FILTER);
            }
        }
    }

    private LectureListResponseDto convertToLectureListDto(Lecture lecture) {
        LectureListResponseDto dto = new LectureListResponseDto();
        dto.setId(lecture.getId());
        dto.setCourseName(lecture.getCourseName());
        dto.setProfessor(lecture.getProfessor());
        dto.setCredit(lecture.getCredit());
        dto.setMaxCapacity(lecture.getMaxCapacity());
        dto.setCurrentCount(lecture.getCurrentCount());
        dto.setDivision(lecture.getDivision());
        dto.setRoom(lecture.getRoom());
        return dto;
    }

    private LectureTimeResponseDto convertToLectureTimeDto(LectureTime time) {
        LectureTimeResponseDto dto = new LectureTimeResponseDto();
        dto.setDayOfWeek(time.getDayOfWeek());
        dto.setStartTime(
                time.getStartTime() != null ? time.getStartTime().format(TIME_FORMATTER) : null
        );
        dto.setEndTime(
                time.getEndTime() != null ? time.getEndTime().format(TIME_FORMATTER) : null
        );
        return dto;
    }
}
