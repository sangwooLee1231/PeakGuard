package com.sku.lecture.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LectureListResponseDto {

    private Long id;                 // 강의 ID
    private String courseName;       // 강의명
    private String professor;        // 교수명
    private Integer credit;          // 학점
    private Integer maxCapacity;     // 최대 정원
    private Integer currentCount;    // 현재 신청 인원
    private String division;         // 이수구분
    private String room;             // 강의실
}
