package com.first_class.msa.product.application.global.exception.handler;

import com.first_class.msa.product.application.global.exception.custom.AuthorityException;
import com.first_class.msa.product.application.global.exception.custom.BadRequestException;
import com.first_class.msa.product.application.global.exception.custom.EntityAlreadyExistException;
import com.first_class.msa.product.application.global.exception.custom.ExceptionResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j(topic = "GlobalExceptionHandler Log")
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityAlreadyExistException.class)
    public ResponseEntity<ExceptionResponseDTO<Object>> handleEntityAlreadyExistException(Exception e) {
        log.error("EntityAlreadyExistException 발생: {}", e.getMessage(), e);
        return new ResponseEntity<>(
                ExceptionResponseDTO.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponseDTO<Object>> handleBadRequestException(Exception e) {
        log.error("BadRequestException 발생: {}", e.getMessage(), e);
        return new ResponseEntity<>(
                ExceptionResponseDTO.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(AuthorityException.class)
    public ResponseEntity<ExceptionResponseDTO<Object>> handleAuthorityException(Exception e) {
        log.error("AuthorityException 발생: {}", e.getMessage(), e);
        return new ResponseEntity<>(
                ExceptionResponseDTO.builder()
                        .code(HttpStatus.FORBIDDEN.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponseDTO<Object>> handleDataIntegrityViolationException(Exception e) {
        log.error("DataIntegrityViolationException 발생: {}", e.getMessage(), e);
        return new ResponseEntity<>(
                ExceptionResponseDTO.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("데이터 무결성 오류가 발생했습니다. 요청하신 작업을 처리할 수 없습니다.")
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }
}
