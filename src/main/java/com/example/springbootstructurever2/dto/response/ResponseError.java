package com.example.springbootstructurever2.dto.response;

public class ResponseError extends ResponseData<Object> {
    public ResponseError(int status, String message) {
        super(status, message);
    }
}
