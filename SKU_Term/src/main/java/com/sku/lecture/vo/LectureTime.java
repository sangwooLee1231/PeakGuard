package com.sku.lecture.vo;

import lombok.Data;

import java.time.LocalTime;

@Data
public class LectureTime {

    private Long id;             // LECTURETIME_ID
    private Long lectureId;      // LECTURETIME_LECTURE_ID
    private String dayOfWeek;    // LECTURETIME_DAY_OF_WEEK (MON, TUE...)
    private LocalTime startTime; // LECTURETIME_START_TIME
    private LocalTime endTime;   // LECTURETIME_END_TIME
}
