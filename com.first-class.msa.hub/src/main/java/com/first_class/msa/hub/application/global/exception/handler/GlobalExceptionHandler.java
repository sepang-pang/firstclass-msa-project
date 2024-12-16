package com.first_class.msa.hub.application.global.exception.handler;

import com.first_class.msa.hub.application.global.exception.custom.AuthorityException;
import com.first_class.msa.hub.application.global.exception.custom.BadRequestException;
import com.first_class.msa.hub.application.global.exception.custom.EntityAlreadyExistException;
import com.first_class.msa.hub.application.global.exception.custom.ExceptionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityAlreadyExistException.class)
    public ResponseEntity<ExceptionResponseDTO<Object>> handleEntityAlreadyExistException(Exception e) {
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
        return new ResponseEntity<>(
                ExceptionResponseDTO.builder()
                        .code(HttpStatus.FORBIDDEN.value())
                        .message(e.getMessage())
                        .build(),
                HttpStatus.FORBIDDEN
        );
    }
}
