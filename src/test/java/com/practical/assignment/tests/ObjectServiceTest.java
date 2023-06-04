package com.practical.assignment.tests;

import com.practical.assignment.constants.StaticHeaders;
import com.practical.assignment.requestDTO.ObjectRequestDTO;
import com.practical.assignment.responseDTO.DeleteResponseDTO;
import com.practical.assignment.responseDTO.ObjectResponseDTO;
import com.practical.assignment.services.ObjectService;
import com.practical.assignment.util.TestBase;
import io.restassured.http.Headers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.Map;
import java.util.TreeMap;

public class ObjectServiceTest extends TestBase {

    private ObjectService objectService;
    private ObjectRequestDTO objectRequestDTO;
    private ObjectResponseDTO objectResponseDTO;

    @BeforeClass
    public void serviceSetUp() {

        try {
            objectService = new ObjectService(testData.get("objectService.host"));
            headers = new Headers(StaticHeaders.CONTENT_TYPE_JSON);

        }catch (Exception e){
            throw e;
        }
    }

    @Test
    public void createObject() throws Exception {

        Map<String, String> data = new TreeMap<>();
        data.put("model", faker.camera().model());
        objectRequestDTO = new ObjectRequestDTO(faker.camera().brandWithModel(), data);

        objectResponseDTO = (ObjectResponseDTO) objectService.createObject(objectRequestDTO, headers, ObjectResponseDTO.class);
        logger.info(objectResponseDTO.getResponse().prettyPrint());
        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "createObject")
    public void updateObject() throws Exception {

        Map<String, String> data = new TreeMap<>();
        data.put("brand", faker.camera().brand());
        data.put("color", faker.color().name());
        objectRequestDTO.setData(data);

        objectResponseDTO = (ObjectResponseDTO) objectService.updateObject(objectResponseDTO.getId(), objectRequestDTO, headers, ObjectResponseDTO.class);
        softAssert.assertEquals(objectResponseDTO.getStatusCode(), 201);
        logger.info(objectResponseDTO.getResponse().prettyPrint());
        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "updateObject")
    public void getObject() throws Exception {

        objectResponseDTO = (ObjectResponseDTO) objectService.retrieveObject(objectResponseDTO.getId(), ObjectResponseDTO.class);
        logger.info(objectResponseDTO.getResponse().prettyPrint());
        softAssert.assertAll();
    }

    @Test(dependsOnMethods = "getObject")
    public void deleteObject() throws Exception {

        DeleteResponseDTO deleteResponseDTO = (DeleteResponseDTO) objectService.deleteObject(objectResponseDTO.getId(), DeleteResponseDTO.class);
        logger.info(deleteResponseDTO.getResponse().prettyPrint());
        softAssert.assertAll();
    }
}
