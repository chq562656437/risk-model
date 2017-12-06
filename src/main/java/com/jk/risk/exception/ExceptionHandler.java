package com.jk.risk.exception;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionHandler implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
    @Value("#{config['alarm.enable']}")
    private boolean alarmEnable;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ErrorEnum err = ExceptionMap.convertThrowable(ex);
        String errorDetails = ExceptionUtil.print(request, ex);
        String errorBrief = Throwables.getRootCause(ex).getMessage();
        if (err == ErrorEnum.InternalSystemError) {
            logger.error(errorDetails);
            if (alarmEnable) {
//                TODO 报警
//                // 邮件报警
//                MonitorSDK.sendAlarm("money.error.mail", Alarm.Level.ERROR, Alarm.Type.EMAIL, errorDetails);
//                // 短信报警
//                MonitorSDK.sendAlarm("money.error.sms", Alarm.Level.ERROR, Alarm.Type.SMS, errorBrief);
            }
        } else if (err == ErrorEnum.NotFound) {
            logger.error(errorBrief);
        } else {
            logger.error(errorDetails);
        }
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(err.httpCode);
        try {
            response.getWriter().write(err.toString());
            response.flushBuffer();
        } catch (IOException ignore) {
        }
        return new ModelAndView();
    }
}
