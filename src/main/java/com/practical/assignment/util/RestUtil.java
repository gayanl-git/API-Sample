package com.practical.assignment.util;

import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class RestUtil {

    protected static final Logger logger = LogManager.getLogger(RestUtil.class.getName());

    public static void setBaseURI (String baseURI){
        RestAssured.baseURI = baseURI;
    }

    public static void setBasePath(String basePath){
        RestAssured.basePath = basePath;
    }

    public static void resetBaseURI (){
        RestAssured.baseURI = null;
    }

    public static void resetBasePath(){
        RestAssured.basePath = null;
    }
    
}