package com.practical.assignment.constants;

public interface Constants {

    String DEFAULT_CONFIG_LOCATION = "src/test/resources/config/config.cfg";

    String UNSUPPORTED_MEDIA_TYPE_MESSAGE = "415 Unsupported Media Type. The 415 status code indicates that the origin server is refusing to service the request because the payload is in a format not supported by this method on the target resource. One of the examples of getting 415 would be sending a request with a Content-Type header which is not equal to application/json";
    String METHOD_NOT_ALLOWED_ERROR_MESSAGE = "Method Not Allowed";

    String OBJECT_DELETE_SUCCESS_MESSAGE = "Object with id = {id} has been deleted.";
    String OBJECT_RETRIEVE_ERROR_MESSAGE = "Object with id={id} was not found.";
    String OBJECT_UPDATE_ERROR_MESSAGE = "The Object with id = {id} doesn't exist. Please provide an object id which exists or generate a new Object using POST request and capture the id of it to use it as part of PUT request after that.";
    String OBJECT_DELETE_ERROR_MESSAGE = "Object with id = {id} doesn't exist.";

}
