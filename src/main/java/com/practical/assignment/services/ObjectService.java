package com.practical.assignment.services;

import com.practical.assignment.constants.RelativeURLs;
import com.practical.assignment.requestDTO.ObjectRequestDTO;
import com.practical.assignment.util.APIServicesBase;
import com.practical.assignment.util.BaseResponseDTO;
import com.practical.assignment.util.RestUtil;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.response.Response;

public class ObjectService extends APIServicesBase {

	private String baseURI;

	public ObjectService(String baseURI) {
		this.baseURI = baseURI;
	}

	public BaseResponseDTO createObject(ObjectRequestDTO body, Headers headers, Class<?> classType) throws Exception {

		try {
			String requestBody = objectMapper.writeValueAsString(body);
			return createObject(requestBody, headers, classType, Method.POST);

		} catch (Exception e) {
			throw e;
		}
	}

	public BaseResponseDTO createObject(String body, Headers headers, Class<?> classType, Method method) throws Exception {

		try {
			// baseURI set here to support parallel execution if needed
			setRequest(baseURI, RelativeURLs.OBJECT_CREATE, body, headers);

			// Make Request
			Response response = makeRequest(body, headers, method);

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
			String requestBody = objectMapper.writeValueAsString(body);
			return updateObject(id, requestBody, headers, classType, Method.PUT);

		} catch (Exception e) {
			throw e;
		}
	}

	public BaseResponseDTO updateObject(String id, String body, Headers headers, Class<?> classType, Method method) throws Exception {

		try {
			// baseURI set here to support dynamic execution if needed
			setRequest(baseURI, RelativeURLs.OBJECT_UPDATE.replace("{id}", id), body, headers);

			// Make Request
			Response response = makeRequest(body, headers, method);

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
			return retrieveObject(id, classType, Method.GET);

		} catch (Exception e) {
			throw e;
		}
	}

	public BaseResponseDTO retrieveObject(String id, Class<?> classType, Method method) throws Exception {

		try {
			// baseURI set here to support dynamic execution if needed
			setRequest(baseURI, RelativeURLs.OBJECT_RETRIEVE.replace("{id}", id));

			// Make Request
			Response response = makeRequest(method);;

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
			return deleteObject(id, classType, Method.DELETE);

		} catch (Exception e) {
			throw e;
		}
	}

	public BaseResponseDTO deleteObject(String id, Class<?> classType, Method method) throws Exception {

		try {
			// baseURI set here to support dynamic execution if needed
			setRequest(baseURI, RelativeURLs.OBJECT_DELETE.replace("{id}", id));

			// Make Request
			Response response = makeRequest(method);

			// Clear Base Path & URI
			RestUtil.resetBasePath();
			RestUtil.resetBaseURI();

			return convertResponseToType(response, classType);

		} catch (Exception e) {
			throw e;
		}
	}

}