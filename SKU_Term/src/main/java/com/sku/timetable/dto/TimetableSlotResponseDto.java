package com.sku.timetable.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 프론트에서 주간 시간표(9~18시)를 그릴 때 사용하는 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimetableSlotResponseDto {

    private Long lectureId;      // 강의 상세로 이동용 (F-403)
    private String courseName;   // 강의명
    private String professor;    // 교수명
    private Integer credit;      // 학점
    private String division;     // 이수구분
    private String room;         // 강의실

    private String dayOfWeek;    // 요일 (MON, TUE, ...)
    private String startTime;    // 시작 시간 (HH:mm, 정각)
    private String endTime;      // 종료 시간 (HH:mm, 45분 수업 기준)
    private Integer period;      // 교시 번호 (1 ~ 10 정도)
}
