package com.jk.risk.exception;


import com.google.common.collect.Maps;
import com.google.common.util.concurrent.UncheckedExecutionException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

public class ExceptionMap {

    private static Map<Class<? extends Throwable>, ErrorEnum> frameworkErrs = Maps.newHashMap();

    static {
        frameworkErrs.put(IllegalArgumentException.class, ErrorEnum.InvalidArgument);
        frameworkErrs.put(HttpRequestMethodNotSupportedException.class, ErrorEnum.MethodNotAllowed);
        frameworkErrs.put(HttpMessageNotReadableException.class, ErrorEnum.BadRequest);
        frameworkErrs.put(MissingServletRequestParameterException.class, ErrorEnum.MissingParameter);
        frameworkErrs.put(MethodArgumentTypeMismatchException.class, ErrorEnum.InvalidArgument);
        frameworkErrs.put(DuplicateKeyException.class, ErrorEnum.OperationAborted);
        frameworkErrs.put(ServletRequestBindingException.class, ErrorEnum.BadRequest);
        frameworkErrs.put(UncheckedExecutionException.class, ErrorEnum.BadRequest);
        frameworkErrs.put(HttpMediaTypeNotSupportedException.class, ErrorEnum.MediaTypeNotSupport);
        frameworkErrs.put(BindException.class, ErrorEnum.InvalidArgument);
    }

    public static ErrorEnum convertThrowable(Throwable e) {
        if (e instanceof RiskSystemException) {
            RiskSystemException exception = (RiskSystemException) e;
            return exception.getErr();
        }
        return frameworkErrs.getOrDefault(e.getClass(), ErrorEnum.InternalSystemError);
    }
}
