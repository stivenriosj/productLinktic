package com.co.service.product.jsonapi;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JsonApiResponse<T> {
	public JsonApiResponse(Map<String, JsonApiData> of) {
	}
}