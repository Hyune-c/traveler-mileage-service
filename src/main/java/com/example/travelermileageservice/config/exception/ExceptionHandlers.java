package com.example.travelermileageservice.config.exception;

import com.example.travelermileageservice.domain.base.exception.BusinessException;
import com.example.travelermileageservice.domain.review.service.exception.ReviewAddException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public Map<String, Object> handleBusinessException(final BusinessException exception) {
        log.error(exception.getMessage(), exception);
        final Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("timestamp", LocalDateTime.now());
        errorAttributes.put("status", HttpStatus.BAD_REQUEST.value());
        errorAttributes.put("error", exception.getMessage());
        return errorAttributes;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ReviewAddException.class)
    public Map<String, Object> handleReviewAddException(final ReviewAddException exception) {
        log.error(exception.getMessage(), exception);
        final String code = Optional.ofNullable(exception.getErrors().getFieldError())
                .orElse(new FieldError("", "", ""))
                .getCode();
        final Map<String, String> error = exception.getErrors().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));

        final Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("timestamp", LocalDateTime.now());
        errorAttributes.put("status", HttpStatus.BAD_REQUEST.value());
        errorAttributes.put("code", code);
        errorAttributes.put("error", error);
        return errorAttributes;
    }
}
