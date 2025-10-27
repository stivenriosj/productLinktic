package com.co.service.product.app.jsonapi;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonApiData<T> {
	private String type;
    private String id;
    private T attributes;
    @SuppressWarnings("unchecked")
	public JsonApiData(String type, String id, Map<String, Object> attributes) {
		this.setType(type);
		this.setId(id);
		this.setAttributes((T) attributes);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public T getAttributes() {
		return attributes;
	}
	public void setAttributes(T attributes) {
		this.attributes = attributes;
	}
	
}
