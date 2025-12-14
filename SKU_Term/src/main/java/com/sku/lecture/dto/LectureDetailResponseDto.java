package com.sku.lecture.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureDetailResponseDto {

    private Long id;                       // 강의 ID
    private String courseName;             // 강의명
    private String professor;              // 교수명
    private Integer credit;                // 학점
    private Integer maxCapacity;           // 최대 정원
    private Integer currentCount;          // 현재 신청 인원
    private String division;               // 이수구분
    private String room;                   // 강의실

    private List<LectureTimeResponseDto> times; // 강의 시간 목록
}
