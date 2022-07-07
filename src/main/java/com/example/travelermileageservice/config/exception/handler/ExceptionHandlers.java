package com.example.travelermileageservice.config.exception.handler;

import com.example.travelermileageservice.config.exception.BusinessException;
import com.example.travelermileageservice.config.exception.CustomValidationException;
import com.example.travelermileageservice.config.exception.ErrorCode;
import com.example.travelermileageservice.config.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(final HttpMessageNotReadableException ex) {
        preHandle(ex);
        return ErrorResponse.of(ex.getMessage());
    }

    /**
     * ModelAttribute 에 binding error 발생시 BindException 발생한다.
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBindException(final BindException ex) {
        preHandle(ex);
        return ErrorResponse.of(ErrorCode.BAD_REQUEST, ex.getBindingResult());
    }

    @ExceptionHandler(CustomValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCustomValidationException(final CustomValidationException ex) {
        preHandle(ex);
        return ErrorResponse.of(ErrorCode.BAD_REQUEST, ex.getBindingResult());
    }

    /**
     * 필요한 param 값이 들어오지 않았을 때 발생합니다.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingServletRequestParameterException(
            final MissingServletRequestParameterException ex) {
        preHandle(ex);
        return ErrorResponse.of(ex);
    }

    /**
     * type 이 일치하지 않아 binding 못할 경우 발생합니다.
     * 주로 @RequestParam enum 으로 binding 못했을 경우 발생합니다.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException ex) {
        preHandle(ex);
        return ErrorResponse.of(ex);
    }

    /**
     * javax.validation 을 통과하지 못하면 에러가 발생합니다.
     * 주로 @NotBlank, @NotEmpty, @NotNull 에서 발생합니다.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(final ConstraintViolationException ex) {
        preHandle(ex);
        return ErrorResponse.of(ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(final IllegalArgumentException ex) {
        preHandle(ex);
        return ErrorResponse.of(ErrorCode.BAD_REQUEST);
    }

    /**
     * Valid or Validated 으로 binding error 발생시 발생합니다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 이 실패하는 경우 발생합니다.
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생합니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        preHandle(ex);
        return ErrorResponse.of(ErrorCode.BAD_REQUEST, ex.getBindingResult());
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException ex) {
        preHandle(ex);
        return new ResponseEntity<>(ErrorResponse.of(ex.getErrorCode()), ex.getErrorCode().getStatus());
    }

    public void preHandle(final Exception ex) {
        log.error("### message={}, cause={}", ex.getMessage(), ex.getCause(), ex);
    }
}
