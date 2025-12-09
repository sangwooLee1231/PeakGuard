package com.sku.lecture.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureVO {

    private Long lectureId;             // LECTURE_ID
    private String lectureCourseName;   // LECTURE_COURSE_NAME
    private String lectureProfessor;    // LECTURE_PROFESSOR
    private Integer lectureCredit;      // LECTURE_CREDIT
    private Integer lectureMaxCapacity; // LECTURE_MAX_CAPACITY
    private Integer lectureCurrentCount;// LECTURE_CURRENT_COUNT
    private String lectureDivision;     // LECTURE_DIVISION
    private String lectureRoom;         // LECTURE_ROOM
}