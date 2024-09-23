package com.xpanse.los.service;

import java.net.http.HttpResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xpanse.los.handler.HttpClientHandler;
import com.xpanse.los.model.ExperianPodiumRequest;
import com.xpanse.los.model.IncomeRequestModel;

import software.amazon.awssdk.utils.StringUtils;

public class ResponseService {

	private static final Logger logger = LogManager.getLogger(ResponseService.class);

	public void getResponse(String apiUrl, String requestBody) {

		logger.info("START getResponse()");

		HttpClientHandler httpClientHandler = new HttpClientHandler();

		try {
			if(StringUtils.isNotBlank(requestBody)) {

				ObjectMapper objectMapper = new ObjectMapper();
				IncomeRequestModel incomeRequestModel = objectMapper.readValue(requestBody, IncomeRequestModel.class);

				logger.info("IncomeRequestModel {}",incomeRequestModel);

				if(incomeRequestModel != null) {

					String requestJSONStr = prepareRequest(incomeRequestModel, objectMapper);

					if(requestJSONStr != null) {

						HttpResponse<String> httpResponse = httpClientHandler.executeHttpRequest(apiUrl, requestJSONStr);
						// Process the response
						String responseBody = httpResponse.body();
						int statusCode = httpResponse.statusCode();

						logger.info("Response from API Gateway : {}, Status Code {}", responseBody, statusCode);
					}
					logger.info("END getResponse()");
				}
			}
		} catch (Exception e) {
			logger.error("Exception occured in getResponse() error {}", e.getMessage());
		}
	}

	private String prepareRequest(IncomeRequestModel incomeRequestModel, ObjectMapper objectMapper) {

		try {
			ExperianPodiumRequest experianPodiumRequest = new ExperianPodiumRequest();

			experianPodiumRequest.setFirstName(incomeRequestModel.getFirstName());
			experianPodiumRequest.setMiddleName(incomeRequestModel.getMiddleName());
			experianPodiumRequest.setLastName(incomeRequestModel.getLastName());
			experianPodiumRequest.setSsn(incomeRequestModel.getSsn());

			logger.info("ExperianPodiumRequest {}",experianPodiumRequest);

			return objectMapper.writeValueAsString(experianPodiumRequest);
		}
		catch (JsonProcessingException jsonProcessingException) {
			logger.error("JsonProcessingException occured in getResponse() error {}", jsonProcessingException.getMessage());
		}
		return null;
	}
}
