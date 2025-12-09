package com.sku.common.service;

import com.sku.member.vo.Student;
import com.sku.member.mapper.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentMapper studentMapper;

    @Override
    public UserDetails loadUserByUsername(String studentNumber) throws UsernameNotFoundException {
        Student student = studentMapper.findByStudentNumber(studentNumber);

        if (student == null) {
            throw new UsernameNotFoundException("해당 학번의 학생을 찾을 수 없습니다: " + studentNumber);
        }

        return createUserDetails(student);
    }

    private UserDetails createUserDetails(Student student) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(student.getRole());

        return new User(
                student.getStudentNumber(),
                student.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}