package com.co.service.product.app.jsonapi;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JsonApiError {
    private String status;
    private String title;
    private String detail;
}