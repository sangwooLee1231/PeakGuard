package com.sku.common.util;

/**
 * 시스템에서 발생할 수 있는 다양한 에러 코드와 메시지를 정의하는 Enum 클래스
 * <p>
 * 수강신청 시스템 전용 에러 관리
 */
public enum ErrorCode {

    // =================================== 공통 시스템 에러 (Common) ===================================
    INVALID_INPUT_VALUE(400, "CM001", "입력값이 올바르지 않습니다."),
    METHOD_NOT_ALLOWED(405, "CM002", "허용되지 않은 HTTP 메서드입니다."),
    ENTITY_NOT_FOUND(404, "CM003", "대상을 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "CM004", "서버 내부 오류가 발생했습니다."),
    INVALID_TYPE_VALUE(400, "CM005", "유효하지 않은 데이터 타입입니다."),
    ACCESS_DENIED(403, "CM006", "접근 권한이 없습니다."),
    DB_CONNECTION_ERROR(500, "CM007", "데이터베이스 연결에 실패했습니다."),

    // =================================== 인증/회원 에러 (Auth & Member) ===================================
    STUDENT_NOT_FOUND(404, "AU001", "존재하지 않는 학번입니다."),
    PASSWORD_MISMATCH(401, "AU002", "비밀번호가 일치하지 않습니다."),
    LOGIN_REQUIRED(401, "AU003", "로그인이 필요한 서비스입니다."),
    SESSION_EXPIRED(401, "AU004", "세션이 만료되었습니다. 다시 로그인해주세요."),
    UNAUTHORIZED_ACCESS(403, "AU005", "해당 기능에 대한 접근 권한이 없습니다."),
    DUPLICATE_STUDENT_NUMBER(409, "AU006", "이미 가입된 학번입니다."),
    STUDENT_SAVE_FAILED(500, "AU007", "회원가입 처리 중 오류가 발생했습니다."),

    // =================================== 강의/시간표 에러 (Lecture) ===================================
    LECTURE_NOT_FOUND(404, "LC001", "해당 강의를 찾을 수 없습니다."),
    LECTURE_SEARCH_FAILED(500, "LC002", "강의 목록 조회 중 오류가 발생했습니다."),
    TIMETABLE_LOAD_FAILED(500, "LC003", "시간표를 불러오는 중 오류가 발생했습니다."),
    LECTURE_INVALID_FILTER(400, "LC004", "유효하지 않은 강의 검색/필터 조건입니다."),


    // =================================== 수강신청 에러 (Enrollment) ===================================
    ENROLLMENT_CAPACITY_FULL(409, "EN001", "정원이 초과되었습니다."),
    ALREADY_ENROLLED(409, "EN002", "이미 신청한 강의입니다."),
    TIME_CONFLICT(409, "EN003", "강의 시간이 기존 시간표와 중복됩니다."),
    CREDIT_EXCEEDED(400, "EN004", "최대 수강 가능 학점을 초과했습니다."),
    ENROLLMENT_NOT_FOUND(404, "EN005", "수강 신청 내역을 찾을 수 없습니다."),
    CANCEL_PERIOD_EXPIRED(400, "EN006", "수강 취소 가능 기간이 아닙니다."),
    ENROLLMENT_FAILED(500, "EN007", "수강 신청 처리 중 시스템 오류가 발생했습니다."),

    // =================================== 장바구니 에러 (Cart) ===================================
    CART_ALREADY_EXISTS(409, "CT001", "이미 장바구니에 담긴 강의입니다."),
    CART_NOT_FOUND(404, "CT002", "장바구니에서 해당 강의를 찾을 수 없습니다."),
    CART_INSERT_FAILED(500, "CT003", "장바구니 담기 중 오류가 발생했습니다."),
    CART_DELETE_FAILED(500, "CT004", "장바구니 삭제 중 오류가 발생했습니다.");

    private final int status;
    private final String code;
    private final String msg;

    /**
     * 에러 코드 및 메시지를 초기화합니다.
     *
     * @param status HTTP 상태 코드 (int)
     * @param code   에러 코드 (예외 원인 식별용)
     * @param msg    사용자에게 보여줄 에러 메시지
     */
    ErrorCode(int status, String code, String msg) {
        this.status = status;
        this.code = code;
        this.msg = msg;
    }

    /**
     * 상태 코드를 반환합니다.
     *
     * @return 상태 코드
     */
    public int getStatus() {
        return status;
    }

    /**
     * 에러 코드를 반환합니다.
     *
     * @return 에러 코드
     */
    public String getCode() {
        return code;
    }

    /**
     * 에러 메시지를 반환합니다.
     *
     * @return 에러 메시지
     */
    public String getMsg() {
        return msg;
    }
}