package com.co.service.product.app.jsonapi;

import java.util.Map;

import lombok.Data;

@Data
public class JsonApiResponse<T> {

    private Map<String, Object> data;

    public JsonApiResponse(Map<String, Object> data) {
        this.data = data;
    }
}