package com.sku.cart.service.serviceImpl;

import com.sku.cart.dto.CartEnrollResultDto;
import com.sku.cart.dto.CartItemResponseDto;
import com.sku.cart.mapper.CartMapper;
import com.sku.cart.service.CartService;
import com.sku.common.exception.CustomException;
import com.sku.common.util.ErrorCode;
import com.sku.enrollment.service.EnrollmentService;
import com.sku.lecture.mapper.LectureMapper;
import com.sku.lecture.vo.Lecture;
import com.sku.member.mapper.StudentMapper;
import com.sku.member.vo.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final StudentMapper studentMapper;
    private final LectureMapper lectureMapper;
    private final EnrollmentService enrollmentService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addToCart(String studentNumber, Long lectureId) {

        if (lectureId == null || lectureId <= 0) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 학생 조회
        Student student = studentMapper.findByStudentNumber(studentNumber);
        if (student == null) {
            throw new CustomException(ErrorCode.STUDENT_NOT_FOUND);
        }
        Long studentId = student.getId();

        // 강의 존재 여부 확인
        Lecture lecture = lectureMapper.findById(lectureId);
        if (lecture == null) {
            throw new CustomException(ErrorCode.LECTURE_NOT_FOUND);
        }

        // 이미 장바구니에 담겼는지 확인
        int exists = cartMapper.existsCart(studentId, lectureId);
        if (exists > 0) {
            throw new CustomException(ErrorCode.CART_ALREADY_EXISTS);
        }


        // 장바구니 INSERT
        int inserted = cartMapper.insertCart(studentId, lectureId);
        if (inserted == 0) {
            throw new CustomException(ErrorCode.CART_INSERT_FAILED);
        }

        log.info("장바구니 담기 완료 - studentId={}, lectureId={}", studentId, lectureId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFromCart(String studentNumber, Long lectureId) {

        if (lectureId == null || lectureId <= 0) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 학생 조회
        Student student = studentMapper.findByStudentNumber(studentNumber);
        if (student == null) {
            throw new CustomException(ErrorCode.STUDENT_NOT_FOUND);
        }
        Long studentId = student.getId();

        // 장바구니 항목 존재 여부 (선택적으로 검사)
        int exists = cartMapper.existsCart(studentId, lectureId);
        if (exists == 0) {
            throw new CustomException(ErrorCode.CART_NOT_FOUND);
        }

        // 삭제
        int deleted = cartMapper.deleteCart(studentId, lectureId);
        if (deleted == 0) {
            throw new CustomException(ErrorCode.CART_DELETE_FAILED);
        }

        log.info("장바구니 삭제 완료 - studentId={}, lectureId={}", studentId, lectureId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItemResponseDto> getMyCart(String studentNumber) {

        // 학생 조회
        Student student = studentMapper.findByStudentNumber(studentNumber);
        if (student == null) {
            throw new CustomException(ErrorCode.STUDENT_NOT_FOUND);
        }
        Long studentId = student.getId();

        //  장바구니 목록 조회
        List<CartItemResponseDto> items = cartMapper.findCartItems(studentId);

        // 장바구니가 비어 있는 것은 비정상은 아니므로 예외를 던지지 않는다.
        return items;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<CartEnrollResultDto> enrollFromCart(String studentNumber, List<Long> lectureIds) {

        if (lectureIds == null || lectureIds.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }


        Student student = studentMapper.findByStudentNumber(studentNumber);
        if (student == null) {
            throw new CustomException(ErrorCode.STUDENT_NOT_FOUND);
        }
        Long studentId = student.getId();

        List<CartEnrollResultDto> results = new ArrayList<>();

        for (Long lectureId : lectureIds) {

            if (lectureId == null || lectureId <= 0) {
                results.add(new CartEnrollResultDto(lectureId, false, "유효하지 않은 강의 ID입니다."));
                continue;
            }

            try {
                // 수강신청 시도
                enrollmentService.enroll(studentNumber, lectureId);

                // 수강신청 성공 시, 장바구니에서 제거 (있
                int exists = cartMapper.existsCart(studentId, lectureId);
                if (exists > 0) {
                    cartMapper.deleteCart(studentId, lectureId);
                }

                results.add(new CartEnrollResultDto(lectureId, true, "수강신청 완료"));

            } catch (CustomException e) {
                results.add(new CartEnrollResultDto(lectureId, false, e.getErrorCode().getMsg()));
                log.warn("장바구니 → 수강신청 실패. studentId={}, lectureId={}, reason={}",
                        studentId, lectureId, e.getErrorCode().getMsg());
            }
        }

        return results;
    }
}
