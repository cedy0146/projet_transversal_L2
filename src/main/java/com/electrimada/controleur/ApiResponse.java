package com.electrimada.controleur;

import java.util.HashMap;
import java.util.Map;

public class ApiResponse {
    private boolean success;
    private String message;
    private Object data;
    private Map<String, Object> meta;

    public ApiResponse() {
        this.meta = new HashMap<>();
    }

    public static ApiResponse success(Object data) {
        ApiResponse response = new ApiResponse();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }

    public static ApiResponse success(String message, Object data) {
        ApiResponse response = new ApiResponse();
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static ApiResponse error(String message) {
        ApiResponse response = new ApiResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    public static ApiResponse error(String message, Object data) {
        ApiResponse response = new ApiResponse();
        response.setSuccess(false);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    public void addMeta(String key, Object value) {
        this.meta.put(key, value);
    }
}

