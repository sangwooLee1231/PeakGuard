package com.sku.lecture.dto;

import lombok.Getter;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LectureTimeResponseDto {

    private String dayOfWeek;
    private String startTime;
    private String endTime;
}
