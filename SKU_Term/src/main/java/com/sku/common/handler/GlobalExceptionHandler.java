package com.sku.common.handler;
import com.sku.common.dto.ErrorResponse;
import com.sku.common.exception.CustomException;
import com.sku.common.util.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @Valid / @Validated 가 붙은 객체 바인딩 실패 시 발생 (@RequestBody 등)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        log.error("handleMethodArgumentNotValidException", e);

        // 첫 번째 필드 에러만 메시지로 사용 (필요하면 더 자세히 바꿔도 됨)
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .orElse(ErrorCode.INVALID_INPUT_VALUE.getMsg());

        ErrorResponse response =
                ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, request.getRequestURI(), message);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * @RequestParam, @PathVariable 등에서 타입이 맞지 않을 때 발생
     * (예: enum 파라미터에 이상한 값이 들어온 경우)
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e,
            HttpServletRequest request
    ) {
        log.error("handleMethodArgumentTypeMismatchException", e);

        ErrorResponse response =
                ErrorResponse.of(ErrorCode.INVALID_TYPE_VALUE, request.getRequestURI());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 비즈니스 로직에서 직접 던지는 CustomException 처리
     */
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(
            CustomException e,
            HttpServletRequest request
    ) {
        log.error("handleBusinessException", e);

        ErrorCode errorCode = e.getErrorCode();

        // e.getMessage() 로 상세 메시지를 덮어쓰고 싶을 때
        ErrorResponse response =
                ErrorResponse.of(errorCode, request.getRequestURI(), e.getMessage());

        return new ResponseEntity<>(
                response,
                HttpStatus.valueOf(errorCode.getStatus())
        );
    }

    /**
     * 그 밖의 예상하지 못한 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(
            Exception e,
            HttpServletRequest request
    ) {
        log.error("handleException", e);

        ErrorResponse response =
                ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, request.getRequestURI());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
