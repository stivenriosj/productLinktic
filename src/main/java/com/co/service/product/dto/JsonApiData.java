package com.co.service.product.dto;

import java.util.Map;

public record JsonApiData(String type, String id, Map<String, Object> attributes) {}

