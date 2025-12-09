package com.sku.student.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentVO {

    private Long studentId;          // STUDENT_ID
    private String studentNumber;    // STUDENT_NUMBER
    private String studentPassword;  // STUDENT_PASSWORD
    private String studentName;      // STUDENT_NAME
    private String studentDepartment;// STUDENT_DEPARTMENT
    private Integer studentGrade;    // STUDENT_GRADE
}