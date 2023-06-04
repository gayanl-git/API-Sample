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

public class ObjectServiceDeleteTest extends TestBase {

    private ObjectService objectService;
    private ObjectRequestDTO objectRequestDTO;
    private ObjectResponseDTO objectResponseDTO;
    private ErrorResponseDTO errorResponseDTO;
    private GenericErrorResponseDTO genericErrorResponseDTO;
    private DeleteResponseDTO deleteResponseDTO;
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
    public void createObjectSuccessfullyAndDeleteIt() throws Exception {

        data.clear();
        data.put("model", faker.camera().model());
        data.put("origin", faker.country().name());
        objectRequestDTO = new ObjectRequestDTO(faker.camera().brandWithModel(), data);

        objectResponseDTO = (ObjectResponseDTO) objectService.createObject(objectRequestDTO, headers, ObjectResponseDTO.class);
        id = objectResponseDTO.getId();
        deleteResponseDTO = (DeleteResponseDTO) objectService.deleteObject(id, DeleteResponseDTO.class);

        softAssert.assertEquals(objectResponseDTO.getStatusCode(), 200);
        softAssert.assertEquals(deleteResponseDTO.getMessage(), Constants.OBJECT_DELETE_SUCCESS_MESSAGE.replace("{id}", id));
        softAssert.assertAll();

    }

    @Test
    public void deleteNonExistingObject() throws Exception {

        errorResponseDTO = (ErrorResponseDTO) objectService.deleteObject("111111111", ErrorResponseDTO.class);

        softAssert.assertEquals(errorResponseDTO.getStatusCode(), 404);
        softAssert.assertEquals(errorResponseDTO.getError(), Constants.OBJECT_DELETE_ERROR_MESSAGE.replace("{id}", "111111111"));
        softAssert.assertAll();

    }

    @Test
    public void deleteObjectWithInvalidMethodType() throws Exception {

        genericErrorResponseDTO = (GenericErrorResponseDTO) objectService.deleteObject("111111111", GenericErrorResponseDTO.class, Method.POST);

        softAssert.assertEquals(genericErrorResponseDTO.getStatusCode(), 405);
        softAssert.assertTrue(DateUtil.dateDiffLessThan(genericErrorResponseDTO.getTimestamp(), allowedDateDiffMilliSec));
        softAssert.assertEquals(genericErrorResponseDTO.getStatus(), 405);
        softAssert.assertEquals(genericErrorResponseDTO.getError(), Constants.METHOD_NOT_ALLOWED_ERROR_MESSAGE);
        softAssert.assertEquals(genericErrorResponseDTO.getPath(), RelativeURLs.OBJECT_DELETE.replace("{id}", "111111111"));
        softAssert.assertAll();

    }

    @Test(dependsOnMethods = "createObjectSuccessfullyAndDeleteIt")
    public void deleteAlreadyDeletedObject() throws Exception {

        errorResponseDTO = (ErrorResponseDTO) objectService.deleteObject(id, ErrorResponseDTO.class);

        softAssert.assertEquals(errorResponseDTO.getStatusCode(), 404);
        softAssert.assertEquals(errorResponseDTO.getError(), Constants.OBJECT_DELETE_ERROR_MESSAGE.replace("{id}", id));
        softAssert.assertAll();

    }

}
