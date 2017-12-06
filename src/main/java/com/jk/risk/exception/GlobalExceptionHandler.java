package com.jk.risk.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object globalExceptionHandler(HttpServletRequest request, Exception ex) {
        RiskSystemException err;
        if (ex instanceof RiskSystemException) {
            err = (RiskSystemException) ex;
        } else if (ex instanceof IllegalArgumentException || ex instanceof MethodArgumentTypeMismatchException) {
            err = new RiskSystemException(ErrorEnum.InvalidArgument, ex);
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            err = new RiskSystemException(ErrorEnum.MethodNotAllowed, ex);
        } else if (ex instanceof MissingServletRequestParameterException) {
            err = new RiskSystemException(ErrorEnum.MissingParameter, ex);
        } else {
            err = RiskSystemException.returnException(ex);
        }
        String error = ExceptionUtil.print(request, err);
        if (err.err == ErrorEnum.InternalSystemError) {
            log.error("Hello Error{}", error);
        } else {
            log.warn("RuntimeException {}", error);
        }
        return ResponseEntity.status(err.err.httpCode).body(err.err.toString());
    }
}
