package com.practical.assignment.tests;

import com.practical.assignment.constants.Constants;
import com.practical.assignment.constants.RelativeURLs;
import com.practical.assignment.constants.StaticHeaders;
import com.practical.assignment.requestDTO.ObjectRequestDTO;
import com.practical.assignment.responseDTO.DeleteResponseDTO;
import com.practical.assignment.responseDTO.ErrorResponseDTO;
import com.practical.assignment.responseDTO.GenericErrorResponseDTO;
import com.practical.assignment.responseDTO.ObjectResponseDTO;
import com.practical.assignment.services.ObjectService;
import com.practical.assignment.util.DateUtil;
import com.practical.assignment.util.TestBase;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.TreeMap;

public class ObjectServiceRetrieveTest extends TestBase {

    private ObjectService objectService;
    private ObjectRequestDTO objectRequestDTO;
    private ObjectResponseDTO objectResponseDTO;
    private ErrorResponseDTO errorResponseDTO;
    private GenericErrorResponseDTO genericErrorResponseDTO;
    private String id;
    private Map<String, String> data;
    private Long allowedDateDiffMilliSec;

    @BeforeClass
    public void serviceSetUp() {

        try {
            objectService = new ObjectService(testData.get("objectService.host"));
            headers = new Headers(StaticHeaders.CONTENT_TYPE_JSON);
            data = new TreeMap<>();
            allowedDateDiffMilliSec = 60000L;

        }catch (Exception e){
            throw e;
        }
    }

    @Test
    public void createObjectSuccessfullyAndRetrieveIt() throws Exception {

        data.clear();
        data.put("model", faker.camera().model());
        data.put("origin", faker.country().name());
        objectRequestDTO = new ObjectRequestDTO(faker.camera().brandWithModel(), data);

        objectResponseDTO = (ObjectResponseDTO) objectService.createObject(objectRequestDTO, headers, ObjectResponseDTO.class);
        id = objectResponseDTO.getId();
        objectResponseDTO = (ObjectResponseDTO) objectService.retrieveObject(id, ObjectResponseDTO.class);

        softAssert.assertEquals(objectResponseDTO.getStatusCode(), 200);
        softAssert.assertEquals(objectResponseDTO.getId(), id);
        softAssert.assertEquals(objectResponseDTO.getName(), objectRequestDTO.getName());
        softAssert.assertEquals(objectResponseDTO.getData(), objectRequestDTO.getData());
        softAssert.assertEquals(objectResponseDTO.getUpdatedAt(), null);
        softAssert.assertEquals(objectResponseDTO.getCreatedAt(), null);
        softAssert.assertAll();

    }

    @Test
    public void retrieveNonExistingObject() throws Exception {

        errorResponseDTO = (ErrorResponseDTO) objectService.retrieveObject("111111111", ErrorResponseDTO.class);

        softAssert.assertEquals(errorResponseDTO.getStatusCode(), 404);
        // Actual fail here due to incorrect spelling used in OBJECT_RETRIEVE_ERROR_MESSAGE
        softAssert.assertEquals(errorResponseDTO.getError(), Constants.OBJECT_RETRIEVE_ERROR_MESSAGE.replace("{id}", "111111111"));
        softAssert.assertAll();

    }

    @Test
    public void retrieveObjectWithInvalidMethodType() throws Exception {

        genericErrorResponseDTO = (GenericErrorResponseDTO) objectService.retrieveObject("111111111", GenericErrorResponseDTO.class, Method.POST);

        softAssert.assertEquals(genericErrorResponseDTO.getStatusCode(), 405);
        softAssert.assertTrue(DateUtil.dateDiffLessThan(genericErrorResponseDTO.getTimestamp(), allowedDateDiffMilliSec));
        softAssert.assertEquals(genericErrorResponseDTO.getStatus(), 405);
        softAssert.assertEquals(genericErrorResponseDTO.getError(), Constants.METHOD_NOT_ALLOWED_ERROR_MESSAGE);
        softAssert.assertEquals(genericErrorResponseDTO.getPath(), RelativeURLs.OBJECT_RETRIEVE.replace("{id}", "111111111"));
        softAssert.assertAll();

    }

    @Test(dependsOnMethods = "createObjectSuccessfullyAndRetrieveIt")
    public void retrieveDeletedObject() throws Exception {

        objectService.deleteObject(id, DeleteResponseDTO.class);
        errorResponseDTO = (ErrorResponseDTO) objectService.retrieveObject(id, ErrorResponseDTO.class);

        softAssert.assertEquals(errorResponseDTO.getStatusCode(), 404);
        // Actual fail here due to incorrect spelling used in OBJECT_RETRIEVE_ERROR_MESSAGE
        softAssert.assertEquals(errorResponseDTO.getError(), Constants.OBJECT_RETRIEVE_ERROR_MESSAGE.replace("{id}", id));
        softAssert.assertAll();

    }

}
