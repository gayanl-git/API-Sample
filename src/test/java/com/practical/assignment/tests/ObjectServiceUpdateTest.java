package com.practical.assignment.tests;

import com.practical.assignment.constants.Constants;
import com.practical.assignment.constants.RelativeURLs;
import com.practical.assignment.constants.StaticHeaders;
import com.practical.assignment.requestDTO.InvalidRequestDTO;
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

public class ObjectServiceUpdateTest extends TestBase {

    private ObjectService objectService;
    private ObjectRequestDTO objectRequestDTO;
    private ObjectResponseDTO objectResponseDTO;
    private ErrorResponseDTO errorResponseDTO;
    private GenericErrorResponseDTO genericErrorResponseDTO;
    private Map<String, String> data;
    private String id;
    private Long allowedDateDiffMilliSec;

    @BeforeClass
    public void serviceSetUp() throws Exception {

        try {
            objectService = new ObjectService(testData.get("objectService.host"));
            headers = new Headers(StaticHeaders.CONTENT_TYPE_JSON);
            data = new TreeMap<>();
            allowedDateDiffMilliSec = 60000L;

            data.put("model", faker.camera().model());
            data.put("origin", faker.country().name());
            objectRequestDTO = new ObjectRequestDTO(faker.camera().brandWithModel(), data);
            objectResponseDTO = (ObjectResponseDTO) objectService.createObject(objectRequestDTO, headers, ObjectResponseDTO.class);
            id = objectResponseDTO.getId();

        }catch (Exception e){
            throw e;
        }
    }

    @Test
    public void updateObjectWithoutName() throws Exception {

        data.clear();
        data.put("model", faker.camera().model());
        data.put("origin", faker.country().name());
        objectRequestDTO = new ObjectRequestDTO();
        objectRequestDTO.setData(data);

        objectResponseDTO = (ObjectResponseDTO) objectService.updateObject(id, objectRequestDTO, headers, ObjectResponseDTO.class);

        softAssert.assertEquals(objectResponseDTO.getStatusCode(), 200);
        softAssert.assertEquals(objectResponseDTO.getId(), id);
        softAssert.assertEquals(objectResponseDTO.getName(), null);
        softAssert.assertEquals(objectResponseDTO.getData(), objectRequestDTO.getData());
        softAssert.assertEquals(objectResponseDTO.getCreatedAt(), null);
        softAssert.assertNotEquals(objectResponseDTO.getUpdatedAt(), null);
        softAssert.assertTrue(DateUtil.dateDiffLessThan(objectResponseDTO.getUpdatedAt(), allowedDateDiffMilliSec));
        softAssert.assertAll();
    }

    @Test
    public void updateObjectWithoutData() throws Exception {

        objectRequestDTO = new ObjectRequestDTO();
        objectRequestDTO.setName(faker.camera().brandWithModel());

        objectResponseDTO = (ObjectResponseDTO) objectService.updateObject(id, objectRequestDTO, headers, ObjectResponseDTO.class);

        softAssert.assertEquals(objectResponseDTO.getStatusCode(), 200);
        softAssert.assertEquals(objectResponseDTO.getId(), id);
        softAssert.assertEquals(objectResponseDTO.getName(), objectRequestDTO.getName());
        softAssert.assertEquals(objectResponseDTO.getData(), null);
        softAssert.assertEquals(objectResponseDTO.getCreatedAt(), null);
        softAssert.assertNotEquals(objectResponseDTO.getUpdatedAt(), null);
        softAssert.assertTrue(DateUtil.dateDiffLessThan(objectResponseDTO.getUpdatedAt(), allowedDateDiffMilliSec));
        softAssert.assertAll();
    }

    @Test
    public void updateObjectWithEmptyData() throws Exception {

        data.clear();
        objectRequestDTO = new ObjectRequestDTO(faker.camera().brandWithModel(), data);

        objectResponseDTO = (ObjectResponseDTO) objectService.updateObject(id, objectRequestDTO, headers, ObjectResponseDTO.class);

        softAssert.assertEquals(objectResponseDTO.getStatusCode(), 200);
        softAssert.assertEquals(objectResponseDTO.getId(), id);
        softAssert.assertEquals(objectResponseDTO.getName(), objectRequestDTO.getName());
        softAssert.assertEquals(objectResponseDTO.getData(), objectRequestDTO.getData());
        softAssert.assertEquals(objectResponseDTO.getCreatedAt(), null);
        softAssert.assertNotEquals(objectResponseDTO.getUpdatedAt(), null);
        softAssert.assertTrue(DateUtil.dateDiffLessThan(objectResponseDTO.getUpdatedAt(), allowedDateDiffMilliSec));
        softAssert.assertAll();
    }

    @Test
    public void updateObjectWithInvalidBodyFormat() throws Exception {

        InvalidRequestDTO invalidRequestDTO = new InvalidRequestDTO("Some Stuff");

        objectResponseDTO = (ObjectResponseDTO) objectService.updateObject(id, OBJECT_MAPPER.writeValueAsString(invalidRequestDTO), headers, ObjectResponseDTO.class, Method.PUT);

        // 400 should be status, but as per the system 200 assumption taking as accurate
        softAssert.assertEquals(objectResponseDTO.getStatusCode(), 200);
        softAssert.assertEquals(objectResponseDTO.getId(), id);
        softAssert.assertEquals(objectResponseDTO.getName(), null);
        softAssert.assertEquals(objectResponseDTO.getData(), null);
        softAssert.assertEquals(objectResponseDTO.getCreatedAt(), null);
        softAssert.assertNotEquals(objectResponseDTO.getUpdatedAt(), null);
        softAssert.assertTrue(DateUtil.dateDiffLessThan(objectResponseDTO.getUpdatedAt(), allowedDateDiffMilliSec));
        softAssert.assertAll();
    }

    @Test
    public void updateObjectWithoutHeaders() throws Exception {

        data.clear();
        data.put("model", faker.camera().model());
        data.put("origin", faker.country().name());
        objectRequestDTO = new ObjectRequestDTO(faker.camera().brandWithModel(), data);

        errorResponseDTO = (ErrorResponseDTO) objectService.updateObject(id, objectRequestDTO, new Headers(), ErrorResponseDTO.class);

        softAssert.assertEquals(errorResponseDTO.getStatusCode(), 415);
        softAssert.assertEquals(errorResponseDTO.getError(), Constants.UNSUPPORTED_MEDIA_TYPE_MESSAGE);
        softAssert.assertAll();
    }

    @Test
    public void updateObjectWithInvalidMethodType() throws Exception {

        data.clear();
        data.put("model", faker.camera().model());
        data.put("origin", faker.country().name());
        objectRequestDTO = new ObjectRequestDTO(faker.camera().brandWithModel(), data);

        genericErrorResponseDTO = (GenericErrorResponseDTO) objectService.updateObject(id, OBJECT_MAPPER.writeValueAsString(objectRequestDTO), headers, GenericErrorResponseDTO.class, Method.POST);

        softAssert.assertEquals(genericErrorResponseDTO.getStatusCode(), 405);
        softAssert.assertTrue(DateUtil.dateDiffLessThan(genericErrorResponseDTO.getTimestamp(), allowedDateDiffMilliSec));
        softAssert.assertEquals(genericErrorResponseDTO.getStatus(), 405);
        softAssert.assertEquals(genericErrorResponseDTO.getError(), Constants.METHOD_NOT_ALLOWED_ERROR_MESSAGE);
        softAssert.assertEquals(genericErrorResponseDTO.getPath(), RelativeURLs.OBJECT_UPDATE.replace("{id}", id));
        softAssert.assertAll();
    }

    @Test
    public void updateNonExistingObject() throws Exception {

        data.clear();
        data.put("model", faker.camera().model());
        data.put("origin", faker.country().name());
        objectRequestDTO = new ObjectRequestDTO(faker.camera().brandWithModel(), data);

        errorResponseDTO = (ErrorResponseDTO) objectService.updateObject("11111111", objectRequestDTO, headers, ErrorResponseDTO.class);

        softAssert.assertEquals(errorResponseDTO.getStatusCode(), 404);
        softAssert.assertEquals(errorResponseDTO.getError(), Constants.OBJECT_UPDATE_ERROR_MESSAGE.replace("{id}", "11111111"));
        softAssert.assertAll();
    }

    @Test
    public void updateDeletedObject() throws Exception {

        data.put("model", faker.camera().model());
        data.put("origin", faker.country().name());
        objectRequestDTO = new ObjectRequestDTO(faker.camera().brandWithModel(), data);
        objectResponseDTO = (ObjectResponseDTO) objectService.createObject(objectRequestDTO, headers, ObjectResponseDTO.class);
        objectService.deleteObject(objectResponseDTO.getId(), DeleteResponseDTO.class);

        errorResponseDTO = (ErrorResponseDTO) objectService.updateObject(objectResponseDTO.getId(), objectRequestDTO, headers, ErrorResponseDTO.class);

        softAssert.assertEquals(errorResponseDTO.getStatusCode(), 404);
        softAssert.assertEquals(errorResponseDTO.getError(), Constants.OBJECT_UPDATE_ERROR_MESSAGE.replace("{id}", objectResponseDTO.getId()));
        softAssert.assertAll();
    }

}
