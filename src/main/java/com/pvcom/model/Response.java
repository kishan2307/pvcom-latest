package com.pvcom.model;

import lombok.Data;

@Data
public class Response<T> {
    private String description;
    private int code;
    private T result;

    public Response(String description, int code, T result) {
        this.description = description;
        this.code = code;
        this.result = result;
    }

    public Response() {
    }
}
