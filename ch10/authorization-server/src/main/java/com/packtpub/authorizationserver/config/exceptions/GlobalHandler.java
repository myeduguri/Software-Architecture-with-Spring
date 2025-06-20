package com.packtpub.authorizationserver.config.exceptions;

import com.packtpub.authorizationserver.internal.exceptions.BusinessException;
import com.packtpub.authorizationserver.internal.exceptions.BusinessExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BusinessExceptionResponse<Object>> handleException(BusinessException e) {
        BusinessExceptionResponse<Object> response = new BusinessExceptionResponse<>(
                e.getCode(),
                null,
                e.getLocalizedMessage()
        );

        return ResponseEntity.ok(response);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex) {
        return buildProblemDetail(HttpStatus.UNAUTHORIZED, ex.getMessage(), ex.getMessage());
    }

    @ExceptionHandler(AccountStatusException.class)
    public ProblemDetail handleAccountStatus(AccountStatusException ex) {
        return buildProblemDetail(HttpStatus.FORBIDDEN, ex.getMessage(), "The account is locked");
    }

    @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
    public ProblemDetail handleAccessDenied(Exception ex) {
        return buildProblemDetail(HttpStatus.FORBIDDEN, ex.getMessage(), "You are not authorized to access this resource");
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ProblemDetail handleUnauthorized(HttpClientErrorException.Unauthorized ex) {
        return buildProblemDetail(HttpStatus.UNAUTHORIZED, ex.getMessage(), "The JWT has expired");
    }


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneral(Exception ex) {
        return buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), "Unknown internal server error");
    }

    private ProblemDetail buildProblemDetail(HttpStatus status, String message, String description) {
        var detail = ProblemDetail.forStatusAndDetail(status, message);
        detail.setProperty("description", description);
        return detail;
    }
}
