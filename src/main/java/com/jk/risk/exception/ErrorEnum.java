package com.jk.risk.exception;

import com.alibaba.fastjson.JSONObject;

public enum ErrorEnum {

    BadRequest(400, "BadRequest", "Bad Request Parameter."),
    Unauthorized(401, "Unauthorized", "The request requires user authentication."),
    Forbidden(403, "Forbidden", "The resources is forbidden to visit."),
    NotFound(404, "NotFound", "The specified resource does not exist."),
    MissingContentLength(400, "MissingContentLength", "You must provide the Content-Length HTTP header."),
    MalformedJson(400, "MalformedJson", "The JSON you provided was not well-formed or did not validate against our published schema."),
    FileDataError(400, "FileDataError", "天啦噜, 数据错误吆"),
    RequestTimeout(400, "RequestTimeout", "Your socket connection to the server was not read from or written to within the timeout period."),
    ConnectionLoss(400, "ConnectionLoss", "Your socket connection to the server may be lost."),
    InvalidArgument(400, "InvalidArgument", "One of your provided argument is malformed or otherwise invalid."),
    MethodNotAllowed(405, "MethodNotAllowed", "The specified method is not allowed against this resource."),
    MissingParameter(400, "MissingParameter", "The request you input is missing some required parameters."),
    DataAccessException(400, "DataAccessException", "Zero rows infected for this request."),
    DuplicateKeyException(400, "DuplicateKeyException", "The request had been handled successfully before."),
    InternalSystemError(500, "InternalError", "We encountered an internal handler. Please try again."),
    OperationAborted(409, "OperationAborted", "A conflicting conditional operation is currently in progress against this resource."),
    MediaTypeNotSupport(415, "MediaTypeNotSupport", "The specified content type is not supported."),
    RemoteServiceFailed(400, "RemoteServiceFailed", "The remote server returns un-expected http status code."),
    RemoteServiceError(500, "RemoteServiceError", "The remote server is temporary disabled.");

    public int    httpCode;
    public String errCode;
    public String errMsg;

    ErrorEnum(int httpCode, String code, String message) {
        this.httpCode = httpCode;
        this.errCode = code;
        this.errMsg = message;
    }

    @Override
    public String toString() {
        JSONObject errJson = new JSONObject();
        errJson.put("err", errCode);
        errJson.put("msg", errMsg);
        return errJson.toString();
    }
}
