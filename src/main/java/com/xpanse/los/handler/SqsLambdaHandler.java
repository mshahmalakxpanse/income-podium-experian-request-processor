package com.xpanse.los.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.xpanse.los.service.ResponseService;

public class SqsLambdaHandler implements RequestHandler<SQSEvent, String> {

	private static final Logger logger = LogManager.getLogger(SqsLambdaHandler.class);

	@Override
	public String handleRequest(SQSEvent event, Context context) {

		logger.info("START : handleRequest()");
		
		String apiUrl = System.getenv("apiUrl");

		if(apiUrl != null) {

			// API Gateway URL
			logger.info("apiUrl={}",apiUrl);

			try {
				// Loop through each record in the SQS event
				for (SQSEvent.SQSMessage message : event.getRecords()) {
					// Process each message
					String messageBody = message.getBody();
					logger.info("Message Body: {}", messageBody);

					ResponseService responseService = new ResponseService();
					responseService.getResponse(apiUrl, messageBody);
				}
			}
			catch (Exception e) {
				logger.error("Exception in handleRequest error : {}", e.getMessage());
			}
		}
		logger.info("END : handleRequest()");
		return "Messages processed successfully";
	}
}
