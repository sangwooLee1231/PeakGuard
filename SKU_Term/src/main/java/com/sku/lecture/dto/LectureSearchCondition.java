package com.sku.lecture.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LectureSearchCondition {

    // F-103 검색 조건
    private String courseName;   // 강의명
    private String professor;    // 교수명
    private String division;     // 이수구분
    private String dayOfWeek;    // 요일
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime; // 시작 시간
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;   // 종료 시간
    private Integer credit;      // 학점
}
