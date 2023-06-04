package com.practical.assignment.tests;

import com.practical.assignment.constants.Constants;
import com.practical.assignment.constants.StaticHeaders;
import com.practical.assignment.requestDTO.ObjectRequestDTO;
import com.practical.assignment.responseDTO.DeleteResponseDTO;
import com.practical.assignment.responseDTO.ObjectResponseDTO;
import com.practical.assignment.services.ObjectService;
import com.practical.assignment.util.DateUtil;
import com.practical.assignment.util.TestBase;
import io.restassured.http.Headers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.Map;
import java.util.TreeMap;

public class ObjectServiceContractTest extends TestBase {

    private ObjectService objectService;
    private ObjectRequestDTO objectRequestDTO;
    private ObjectResponseDTO objectResponseDTO;
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
    public void createObject() throws Exception {

        data.put("model", faker.camera().model());
        data.put("origin", faker.country().name());
        objectRequestDTO = new ObjectRequestDTO(faker.camera().brandWithModel(), data);

        objectResponseDTO = (ObjectResponseDTO) objectService.createObject(objectRequestDTO, headers, ObjectResponseDTO.class);
        id = objectResponseDTO.getId();

        // This Should be 201 as it creates entity but as per the system 200 assumption taking as accurate
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

    @Test(dependsOnMethods = "createObject")
    public void updateObject() throws Exception {

        data.put("brand", faker.camera().brand());
        data.put("color", faker.color().name());
        data.put("model", faker.camera().model());
        data.remove("origin");
        objectRequestDTO.setData(data);

        objectResponseDTO = (ObjectResponseDTO) objectService.updateObject(id, objectRequestDTO, headers, ObjectResponseDTO.class);

        softAssert.assertEquals(objectResponseDTO.getStatusCode(), 200);
        softAssert.assertEquals(objectResponseDTO.getId(), id);
        softAssert.assertEquals(objectResponseDTO.getName(), objectRequestDTO.getName());
        softAssert.assertEquals(objectResponseDTO.getData(), objectRequestDTO.getData());
        softAssert.assertNotEquals(objectResponseDTO.getUpdatedAt(), null);
        softAssert.assertEquals(objectResponseDTO.getCreatedAt(), null);
        softAssert.assertTrue(DateUtil.dateDiffLessThan(objectResponseDTO.getUpdatedAt(), allowedDateDiffMilliSec));
        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "updateObject")
    public void getObject() throws Exception {

        objectResponseDTO = (ObjectResponseDTO) objectService.retrieveObject(id, ObjectResponseDTO.class);

        softAssert.assertEquals(objectResponseDTO.getStatusCode(), 200);
        softAssert.assertEquals(objectResponseDTO.getId(), id);
        softAssert.assertEquals(objectResponseDTO.getName(), objectRequestDTO.getName());
        softAssert.assertEquals(objectResponseDTO.getData(), objectRequestDTO.getData());
        softAssert.assertEquals(objectResponseDTO.getUpdatedAt(), null);
        softAssert.assertEquals(objectResponseDTO.getCreatedAt(), null);
        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "getObject")
    public void deleteObject() throws Exception {

        DeleteResponseDTO deleteResponseDTO = (DeleteResponseDTO) objectService.deleteObject(id, DeleteResponseDTO.class);

        softAssert.assertEquals(objectResponseDTO.getStatusCode(), 200);
        softAssert.assertEquals(deleteResponseDTO.getMessage(), Constants.OBJECT_DELETE_SUCCESS_MESSAGE.replace("{id}", id));
        softAssert.assertAll();
    }

}
