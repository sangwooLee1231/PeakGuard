package com.sku.member.service.serviceImpl;

import com.sku.common.exception.CustomException;
import com.sku.common.util.ErrorCode;
import com.sku.member.dto.MemberSignUpRequestDto;
import com.sku.member.mapper.StudentMapper;
import com.sku.member.service.MemberService;
import com.sku.member.vo.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final StudentMapper studentMapper;
    private final PasswordEncoder passwordEncoder;


        @Override
        public Long signUp(MemberSignUpRequestDto requestDto) {

            // 학번 중복 체크
            Student existing = studentMapper.findByStudentNumber(requestDto.getStudentNumber());
            if (existing != null) {
                throw new CustomException(ErrorCode.DUPLICATE_STUDENT_NUMBER);
            }

            Student student = new Student();
            student.setStudentNumber(requestDto.getStudentNumber());
            student.setPassword(passwordEncoder.encode(requestDto.getPassword()));
            student.setName(requestDto.getName());
            student.setDepartment(requestDto.getDepartment());
            student.setGrade(requestDto.getGrade() != null ? requestDto.getGrade() : 1);
            student.setRole("ROLE_STUDENT");

            int inserted = studentMapper.insertStudent(student);
            if (inserted != 1) {
                throw new CustomException(ErrorCode.STUDENT_SAVE_FAILED);
            }

            log.info("회원가입 완료 - studentId={}, studentNumber={}",
                    student.getId(), student.getStudentNumber());

            return student.getId();
        }
    }
