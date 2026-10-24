package org.example.portfolioshowcase.exception;

import org.example.portfolioshowcase.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleBusiness(BusinessException e) {
        return ResponseEntity.status(toHttpStatus(e.getCode())).body(Result.error(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Result<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.badRequest().body(Result.error(40001, "参数错误: " + e.getName()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleOther(Exception e) {
        log.error("Unexpected error", e);
        return ResponseEntity.internalServerError().body(Result.error(50000, "服务器内部错误"));
    }

    private HttpStatus toHttpStatus(int code) {
        return switch (code) {
            case 40001 -> HttpStatus.BAD_REQUEST;
            case 40401 -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.OK;
        };
    }
}
