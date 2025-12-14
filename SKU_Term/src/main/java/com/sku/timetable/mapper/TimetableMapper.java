package com.sku.timetable.mapper;

import com.sku.timetable.vo.TimetableSlot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TimetableMapper {

    /**
     * 개인 시간표 조회
     */
    List<TimetableSlot> findWeeklyTimetable(@Param("studentId") Long studentId);
}
