package com.practical.assignment.tests;

import com.practical.assignment.constants.Constants;
import com.practical.assignment.constants.RelativeURLs;
import com.practical.assignment.constants.StaticHeaders;
import com.practical.assignment.requestDTO.InvalidRequestDTO;
import com.practical.assignment.requestDTO.ObjectRequestDTO;
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

public class ObjectServiceCreateTest extends TestBase {

    private ObjectService objectService;
    private ObjectRequestDTO objectRequestDTO;
    private ObjectResponseDTO objectResponseDTO;
    private ErrorResponseDTO errorResponseDTO;
    private GenericErrorResponseDTO genericErrorResponseDTO;
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
    public void createObjectWithoutName() throws Exception {

        data.clear();
        data.put("model", faker.camera().model());
        data.put("origin", faker.country().name());
        objectRequestDTO = new ObjectRequestDTO();
        objectRequestDTO.setData(data);

        objectResponseDTO = (ObjectResponseDTO) objectService.createObject(objectRequestDTO, headers, ObjectResponseDTO.class);

        softAssert.assertEquals(objectResponseDTO.getStatusCode(), 200);
        softAssert.assertEquals(objectResponseDTO.getName(), null);
        softAssert.assertEquals(objectResponseDTO.getData(), objectRequestDTO.getData());
        softAssert.assertNotEquals(objectResponseDTO.getId(), null);
        softAssert.assertFalse(objectResponseDTO.getId().isBlank());
        softAssert.assertNotEquals(objectResponseDTO.getCreatedAt(), null);
        softAssert.assertEquals(objectResponseDTO.getUpdatedAt(), null);
        softAssert.assertTrue(DateUtil.dateDiffLessThan(objectResponseDTO.getCreatedAt(), allowedDateDiffMilliSec));
        softAssert.assertAll();
    }

    @Test
    public void createObjectWithoutData() throws Exception {

        objectRequestDTO = new ObjectRequestDTO();
        objectRequestDTO.setName(faker.camera().brandWithModel());

        objectResponseDTO = (ObjectResponseDTO) objectService.createObject(objectRequestDTO, headers, ObjectResponseDTO.class);

        softAssert.assertEquals(objectResponseDTO.getStatusCode(), 200);
        softAssert.assertEquals(objectResponseDTO.getName(), objectRequestDTO.getName());
        softAssert.assertEquals(objectResponseDTO.getData(), null);
        softAssert.assertNotEquals(objectResponseDTO.getId(), null);
        softAssert.assertFalse(objectResponseDTO.getId().isBlank());
        softAssert.assertNotEquals(objectResponseDTO.getCreatedAt(), null);
        softAssert.assertEquals(objectResponseDTO.getUpdatedAt(), null);
        softAssert.assertTrue(DateUtil.dateDiffLessThan(objectResponseDTO.getCreatedAt(), allowedDateDiffMilliSec));
        softAssert.assertAll();
    }

    @Test
    public void createObjectWithEmptyData() throws Exception {

        data.clear();
        objectRequestDTO = new ObjectRequestDTO(faker.camera().brandWithModel(), data);

        objectResponseDTO = (ObjectResponseDTO) objectService.createObject(objectRequestDTO, headers, ObjectResponseDTO.class);

        softAssert.assertEquals(objectResponseDTO.getStatusCode(), 200);
        softAssert.assertEquals(objectResponseDTO.getName(), objectRequestDTO.getName());
        softAssert.assertEquals(objectResponseDTO.getData(), objectRequestDTO.getData());
        softAssert.assertNotEquals(objectResponseDTO.getId(), null);
        softAssert.assertFalse(objectResponseDTO.getId().isBlank());
        softAssert.assertNotEquals(objectResponseDTO.getCreatedAt(), null);
        softAssert.assertEquals(objectResponseDTO.getUpdatedAt(), null);
        softAssert.assertTrue(DateUtil.dateDiffLessThan(objectResponseDTO.getCreatedAt(), allowedDateDiffMilliSec));
        softAssert.assertAll();
    }

    @Test
    public void createObjectWithInvalidBodyFormat() throws Exception {

        InvalidRequestDTO invalidRequestDTO = new InvalidRequestDTO("Some Stuff");

        objectResponseDTO = (ObjectResponseDTO) objectService.createObject(OBJECT_MAPPER.writeValueAsString(invalidRequestDTO), headers, ObjectResponseDTO.class, Method.POST);

        // 400 should be status, but as per the system 200 assumption taking as accurate
        softAssert.assertEquals(objectResponseDTO.getStatusCode(), 200);
        softAssert.assertEquals(objectResponseDTO.getName(), null);
        softAssert.assertEquals(objectResponseDTO.getData(), null);
        softAssert.assertNotEquals(objectResponseDTO.getId(), null);
        softAssert.assertFalse(objectResponseDTO.getId().isBlank());
        softAssert.assertNotEquals(objectResponseDTO.getCreatedAt(), null);
        softAssert.assertEquals(objectResponseDTO.getUpdatedAt(), null);
        softAssert.assertTrue(DateUtil.dateDiffLessThan(objectResponseDTO.getCreatedAt(), allowedDateDiffMilliSec));
        softAssert.assertAll();
    }

    @Test
    public void createObjectWithoutHeaders() throws Exception {

        data.clear();
        data.put("model", faker.camera().model());
        data.put("origin", faker.country().name());
        objectRequestDTO = new ObjectRequestDTO(faker.camera().brandWithModel(), data);

        errorResponseDTO = (ErrorResponseDTO) objectService.createObject(objectRequestDTO, new Headers(), ErrorResponseDTO.class);

        softAssert.assertEquals(errorResponseDTO.getStatusCode(), 415);
        softAssert.assertEquals(errorResponseDTO.getError(), Constants.UNSUPPORTED_MEDIA_TYPE_MESSAGE);
        softAssert.assertAll();
    }

    @Test
    public void createObjectWithInvalidMethodType() throws Exception {

        data.clear();
        data.put("model", faker.camera().model());
        data.put("origin", faker.country().name());
        objectRequestDTO = new ObjectRequestDTO(faker.camera().brandWithModel(), data);

        genericErrorResponseDTO = (GenericErrorResponseDTO) objectService.createObject(OBJECT_MAPPER.writeValueAsString(objectRequestDTO), headers, GenericErrorResponseDTO.class, Method.PUT);

        softAssert.assertEquals(genericErrorResponseDTO.getStatusCode(), 405);
        softAssert.assertTrue(DateUtil.dateDiffLessThan(genericErrorResponseDTO.getTimestamp(), allowedDateDiffMilliSec));
        softAssert.assertEquals(genericErrorResponseDTO.getStatus(), 405);
        softAssert.assertEquals(genericErrorResponseDTO.getError(), Constants.METHOD_NOT_ALLOWED_ERROR_MESSAGE);
        softAssert.assertEquals(genericErrorResponseDTO.getPath(), RelativeURLs.OBJECT_CREATE);
        softAssert.assertAll();
    }

}
