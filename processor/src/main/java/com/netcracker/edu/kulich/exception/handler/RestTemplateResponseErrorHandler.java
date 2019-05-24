package com.netcracker.edu.kulich.exception.handler;

import com.netcracker.edu.kulich.exception.remote.RemoteAPIException;
import com.netcracker.edu.kulich.logging.annotation.DefaultLogging;
import com.netcracker.edu.kulich.logging.annotation.Logging;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@DefaultLogging
@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    private final static Logger logger = LoggerFactory.getLogger(RestTemplateResponseErrorHandler.class);

    @Override
    @Logging(startMessage = "Handling remote api exception...", endMessage = "RemoteAPIException thrown.", level = LogLevel.ERROR)
    public void handleError(ClientHttpResponse response) throws IOException {
        String theString = IOUtils.toString(response.getBody(), StandardCharsets.UTF_8.name());
        throw new RemoteAPIException(theString, response.getStatusCode());
    }

    @Override
    public boolean hasError(ClientHttpResponse clienthttpresponse) throws IOException {
        if (clienthttpresponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Response has no errors.");
            return false;
        }
        logger.error("Response has error.");
        return true;
    }
}