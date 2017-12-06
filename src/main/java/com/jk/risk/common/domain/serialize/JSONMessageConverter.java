package com.jk.risk.common.domain.serialize;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.google.common.net.HttpHeaders;
import com.rm.util.JSONUtil;
import com.rm.util.exception.ErrorEnum;
import com.rm.util.exception.RMSystemException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.List;


public class JSONMessageConverter implements GenericHttpMessageConverter<Object> {

    private final static String          JSON_UTF8           = MediaType.APPLICATION_JSON_UTF8_VALUE;
    private final        List<MediaType> supportedMediaTypes = Lists.newArrayList(MediaType.ALL);

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return true;
    }

    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return true;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return true;
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return true;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return supportedMediaTypes;
    }

    @Override
    public Object read(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return read(clazz, null, inputMessage);
    }

    @Override
    public void write(Object o, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        write(o, null, contentType, outputMessage);
    }


    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        Object obj;
        try {
            String body = new String(ByteStreams.toByteArray(inputMessage.getBody()), Charsets.UTF_8);
            if (body.startsWith("{") || body.startsWith("[")) {
                obj = JSONUtil.safeReadByJavaType(body, type);
            } else {
                throw new Exception("Only accept JSON format request body.");
            }
        } catch (Exception e) {
            throw new RMSystemException(ErrorEnum.MalformedJson, e);
        }
        return obj;
    }


    @Override
    public void write(Object o, Type type, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        byte[] json;
        if (o instanceof CommonModel) {
            json = ((CommonModel) o).toJson().getBytes(Charsets.UTF_8);
        } else {
            json = JSONUtil.safeToJson(o).getBytes(Charsets.UTF_8);
        }
        outputMessage.getHeaders().setContentLength(json.length);
        outputMessage.getHeaders().set(HttpHeaders.CONTENT_TYPE, JSON_UTF8);
        try {
            OutputStream os = outputMessage.getBody();
            os.write(json);
            os.flush();
        } catch (IOException ioe) {
            throw new RMSystemException(ErrorEnum.ConnectionLoss, ioe);
        }
    }
}