package com.sku.member.vo;

import lombok.Data;

@Data
public class Student {
    private Long id;              // STUDENT_ID
    private String studentNumber; // STUDENT_NUMBER (로그인 ID)
    private String password;      // STUDENT_PASSWORD
    private String name;          // STUDENT_NAME
    private String department;    // STUDENT_DEPARTMENT
    private int grade;            // STUDENT_GRADE
    private String role;          // STUDENT_ROLE (권한)
}
