package com.practical.assignment.constants;

import io.restassured.http.Header;

public interface StaticHeaders {

    Header CONTENT_TYPE_JSON = new Header("Content-Type", "application/json");

}
