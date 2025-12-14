package com.sku.timetable.vo;

import lombok.Data;

import java.time.LocalTime;

@Data
public class TimetableSlot {

    private Long lectureId;      // 강의 ID
    private String courseName;   // 강의명
    private String professor;    // 교수명
    private Integer credit;      // 학점
    private String division;     // 이수구분
    private String room;         // 강의실

    private String dayOfWeek;    // 요일
    private LocalTime startTime; // 시작 시간
    private LocalTime endTime;   // 종료 시간

    private Integer period;      // 교시 번호
}
