package com.practical.assignment.services;

import com.practical.assignment.constants.RelativeURLs;
import com.practical.assignment.requestDTO.ObjectRequestDTO;
import com.practical.assignment.util.APIServicesBase;
import com.practical.assignment.util.BaseResponseDTO;
import com.practical.assignment.util.RestUtil;
import io.restassured.http.Headers;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ObjectService extends APIServicesBase {

	private String baseURI;
	private String requestBody;

	public ObjectService(String baseURI) {
		this.baseURI = baseURI;
	}

	public BaseResponseDTO createObject(ObjectRequestDTO body, Headers headers, Class<?> classType) throws Exception {

		try {
			// baseURI set here to support dynamic execution if needed
			requestBody = objectMapper.writeValueAsString(body);
			setRequest(baseURI, RelativeURLs.OBJECT_CREATE, requestBody, headers);

			// Make Request
			Response response =
					given()
						.headers(headers)
						.body(requestBody)
					.when()
						.post();

			// Clear Base Path & URI
			RestUtil.resetBasePath();
			RestUtil.resetBaseURI();

			return convertResponseToType(response, classType);

		} catch (Exception e) {
			throw e;
		}
	}

	public BaseResponseDTO updateObject(String id, ObjectRequestDTO body, Headers headers, Class<?> classType) throws Exception {

		try {
			// baseURI set here to support dynamic execution if needed
			requestBody = objectMapper.writeValueAsString(body);
			setRequest(baseURI, RelativeURLs.OBJECT_UPDATE.replace("{id}", id), requestBody, headers);

			// Make Request
			Response response =
					given()
						.headers(headers)
						.body(objectMapper.writeValueAsString(body))
					.when()
						.put();

			// Clear Base Path & URI
			RestUtil.resetBasePath();
			RestUtil.resetBaseURI();

			return convertResponseToType(response, classType);

		} catch (Exception e) {
			throw e;
		}
	}

	public BaseResponseDTO retrieveObject(String id, Class<?> classType) throws Exception {

		try {
			// baseURI set here to support dynamic execution if needed
			setRequest(baseURI, RelativeURLs.OBJECT_RETRIEVE.replace("{id}", id));

			// Make Request
			Response response = given().get();

			// Clear Base Path & URI
			RestUtil.resetBasePath();
			RestUtil.resetBaseURI();

			return convertResponseToType(response, classType);

		} catch (Exception e) {
			throw e;
		}
	}

	public BaseResponseDTO deleteObject(String id, Class<?> classType) throws Exception {

		try {
			// baseURI set here to support dynamic execution if needed
			setRequest(baseURI, RelativeURLs.OBJECT_DELETE.replace("{id}", id));

			// Make Request
			Response response = given().delete();

			// Clear Base Path & URI
			RestUtil.resetBasePath();
			RestUtil.resetBaseURI();

			return convertResponseToType(response, classType);

		} catch (Exception e) {
			throw e;
		}
	}

}