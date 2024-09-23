package com.xpanse.los.handler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpClientHandler {

	private static final Logger logger = LogManager.getLogger(HttpClientHandler.class);

	public HttpResponse<String> executeHttpRequest(String apiUrl, String requestBody) {

		HttpResponse<String> httpResponse = null;
		try {

			logger.info("START : executeHttpRequest()");
			// Create an HttpClient
			HttpClient httpClient = HttpClient.newHttpClient();

			// Create an HttpRequest for POST
			HttpRequest httpRequest = HttpRequest.newBuilder()
					.uri(URI.create(apiUrl))
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
					.build();

			// Send the request
			httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

			logger.info("Response from API Gateway : {}", httpResponse);
			
			logger.info("END : executeHttpRequest()");

		} catch (InterruptedException | IOException e) {
			Thread.currentThread().interrupt();
			logger.error("InterruptedException | IOException occured in handleRequest() error {}", e.getMessage());
		}
		return httpResponse;
	}
}
