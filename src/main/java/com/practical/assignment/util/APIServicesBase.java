package com.practical.assignment.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public abstract class APIServicesBase extends RestUtil {

    protected static final ObjectMapper objectMapper = new ObjectMapper();
    protected static BaseResponseDTO baseResponseDTO;

    public APIServicesBase() {
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public void setRequest(String baseURI, String basePath, String requestBody, Headers headers) throws JsonProcessingException {

        setRequest(baseURI, basePath);
        logger.info("Request Body - \n" + requestBody);
        logger.info("Request Headers - \n" + objectMapper.writeValueAsString(headers));

    }

    public void setRequest(String baseURI, String basePath) {

        RestUtil.setBaseURI(baseURI);
        RestUtil.setBasePath(basePath);

    }

    public BaseResponseDTO convertResponseToType(Response response, Class<?> classType) {

        logger.info("Response Body - \n" + response.asPrettyString());
        logger.info("Response Status - " + response.statusCode());

        try{
            baseResponseDTO = (BaseResponseDTO) objectMapper.readValue(response.asString(), classType);

        } catch (JsonProcessingException e){
            logger.info("Given Response Type Not Matched To Actual Response Got - " + classType);
            e.printStackTrace();
        } catch (Exception e){
            throw e;
        }

        baseResponseDTO.setResponse(response);
        return baseResponseDTO;
    }

    public Response makeRequest(String body, Headers headers, Method method) {

        return given()
                    .headers(headers)
                    .body(body)
                .when()
                    .request(method);
    }

    public Response makeRequest(Method method) {

        return given().request(method);
    }

}
