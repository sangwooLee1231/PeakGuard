package com.sku.common.exception;

import com.sku.common.util.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 회원(학생) 및 인증 관련 예외 클래스
 * (로그인 실패, 세션 만료, 권한 없음 등)
 */
@Getter
@Slf4j
public class SkuLectureException extends CustomException {

    public SkuLectureException(ErrorCode errorCode) {
        super(errorCode);
    }

    public SkuLectureException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}