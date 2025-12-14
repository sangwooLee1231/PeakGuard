package com.sku.lecture.mapper;

import com.sku.lecture.dto.LectureSearchCondition;
import com.sku.lecture.vo.Lecture;
import com.sku.lecture.vo.LectureTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LectureMapper {

    // 강의 목록 조회 (검색 + 필터)
    List<Lecture> findLectures(@Param("cond") LectureSearchCondition condition);

    // 강의 단건 조회
    Lecture findById(@Param("lectureId") Long lectureId);

    // 강의 시간 목록 조회
    List<LectureTime> findTimesByLectureId(@Param("lectureId") Long lectureId);
}
