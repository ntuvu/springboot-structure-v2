package com.example.springbootstructurever2.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@Getter
public class ResponseSuccess extends ResponseEntity<ResponseSuccess.Payload> {

    public ResponseSuccess(HttpStatusCode status, String message) {
        super(new Payload(status.value(), message), status);
    }

    public ResponseSuccess(HttpStatusCode status, String message, Object data) {
        super(new Payload(status.value(), message), status);
    }

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class Payload {
        private final int status;
        private final String message;
        private Object data;
    }
}
