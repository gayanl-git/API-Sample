package com.practical.assignment.util;

import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponseDTO {

    private Response response;
    private int statusCode;
    private String contentType;
    private String body;

    public void setResponse(Response response) {
        this.response = response;
        this.statusCode = response.statusCode();
        this.contentType = response.contentType();
        this.body = response.asString();
    }

}
