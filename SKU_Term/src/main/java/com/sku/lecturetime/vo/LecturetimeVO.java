package com.sku.lecturetime.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LecturetimeVO {


    private Long lecturetimeId;           // LECTURETIME_ID
    private Long lecturetimeLectureId;    // LECTURETIME_LECTURE_ID
    private String lecturetimeDayOfWeek;  // LECTURETIME_DAY_OF_WEEK (MON,TUE,...)
    private LocalTime lecturetimeStartTime; // LECTURETIME_START_TIME
    private LocalTime lecturetimeEndTime;   // LECTURETIME_END_TIME
}