package com.sku.member.mapper;

import com.sku.member.vo.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper {
    Student findByStudentNumber(String studentNumber);

    int insertStudent(Student student);
}