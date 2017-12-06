package com.jk.risk.exception;

import com.google.common.base.Throwables;
import com.jk.risk.util.JSONUtil;

import java.util.Map;

public class RiskSystemException extends RuntimeException {

    public final ErrorEnum err;

    public RiskSystemException(ErrorEnum err, String message) {
        super(err.name() + ":" + message);
        this.err = err;
    }

    public RiskSystemException(ErrorEnum err, Throwable throwable) {
        super(err.name() + ":" + Throwables.getRootCause(throwable).getMessage(), throwable);
        this.err = err;
    }

    public RiskSystemException(Throwable e) {
        super(e);
        this.err = ErrorEnum.InternalSystemError;
    }

    public ErrorEnum getErr() {
        return err;
    }

    public static RiskSystemException returnException(Exception e) {
        try {
            Map<String, Object> error = JSONUtil.read(e.getCause().getMessage(), Map.class);
            if (error.containsKey("code")) {
                ErrorEnum error1 =  ErrorEnum.valueOf(error.get("code").toString());
                return  new RiskSystemException(error1, "");
            }
        } catch (Exception ignored) {
        }
        return new RiskSystemException(ErrorEnum.InternalSystemError, e);
    }

}
