package com.sku.lecture.vo;

import lombok.Data;

@Data
public class Lecture {

    private Long id;                 // LECTURE_ID
    private String courseName;       // LECTURE_COURSE_NAME
    private String professor;        // LECTURE_PROFESSOR
    private Integer credit;          // LECTURE_CREDIT
    private Integer maxCapacity;     // LECTURE_MAX_CAPACITY
    private Integer currentCount;    // LECTURE_CURRENT_COUNT
    private String division;         // LECTURE_DIVISION
    private String room;             // LECTURE_ROOM
}
