package com.sku.lecture.service;

import com.sku.lecture.dto.LectureDetailResponseDto;
import com.sku.lecture.dto.LectureListResponseDto;
import com.sku.lecture.dto.LectureSearchCondition;

import java.util.List;

public interface LectureService {

    List<LectureListResponseDto> getLectures(LectureSearchCondition condition);

    LectureDetailResponseDto getLectureDetail(Long lectureId);
}
