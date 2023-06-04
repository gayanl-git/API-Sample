package com.practical.assignment.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.practical.assignment.constants.RelativeURLs;
import com.practical.assignment.requestDTO.ObjectRequestDTO;
import io.restassured.http.Headers;
import io.restassured.response.Response;

public abstract class APIServicesBase extends RestUtil {

    protected static final ObjectMapper objectMapper = new ObjectMapper();
    protected static BaseResponseDTO baseResponseDTO;

    public APIServicesBase() {
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public void setRequest(String baseURI, String basePath, String requestBody, Headers headers) throws JsonProcessingException {

        setRequest(baseURI, basePath);
        logger.info("Request Body - " + requestBody);
        logger.info("Request Headers - " + objectMapper.writeValueAsString(headers));

    }

    public void setRequest(String baseURI, String basePath) {

        RestUtil.setBaseURI(baseURI);
        RestUtil.setBasePath(basePath);

    }

    public BaseResponseDTO convertResponseToType(Response response, Class<?> classType) {

        try{
            baseResponseDTO = (BaseResponseDTO) objectMapper.readValue(response.asString(), classType);

        } catch (JsonProcessingException e){
            baseResponseDTO.setProperlyDeserialized(false);
            e.printStackTrace();
        } catch (Exception e){
            throw e;
        }

        logger.info("Response Body - " + response.asString());
        logger.info("Response Status - " + response.statusCode());
        baseResponseDTO.setResponse(response);
        baseResponseDTO.setProperlyDeserialized(true);
        return baseResponseDTO;
    }

}
