package com.sku.enrollment.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentVO {

    private Long enrollmentId;         // ENROLLMENT_ID
    private Long enrollmentStudentId;  // ENROLLMENT_STUDENT_ID
    private Long enrollmentLectureId;  // ENROLLMENT_LECTURE_ID
    private LocalDateTime enrollmentCreatedAt; // ENROLLMENT_CREATED_AT
}