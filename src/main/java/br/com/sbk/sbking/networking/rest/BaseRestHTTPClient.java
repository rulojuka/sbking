package br.com.sbk.sbking.networking.rest;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.io.IOException;
import java.util.UUID;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

public abstract class BaseRestHTTPClient {

    protected String baseUrl;
    protected UUID identifier;

    public BaseRestHTTPClient(String ip, UUID identifier) {
        this.baseUrl = String.format("http://%s:8080/", ip);
        this.identifier = identifier;
    }

    public BaseRestHTTPClient(String ip) {
        this(ip, null);
    }

    public void setIdentifier(String identifier) {
        this.identifier = UUID.fromString(identifier);
        LOGGER.info("Setting identifier: {}", this.identifier);
    }

    private String getIdentifierString() {
        if (this.identifier == null) {
            throw new IdentifierNotSetException();
        }
        return identifier.toString();
    }

    protected String createBodyFromContent(String content) {
        return String.format("{\"content\":\"%s\"}", content);
    }

    protected String getWithoutIdentification(String url) {
        LOGGER.info("[GET] (no identification) URL: {}", url);
        String result = null;
        try {
            Request request = Request.get(url);
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-Type", "application/json");
            Response response = request.execute();
            result = response.returnContent().asString();
        } catch (IOException e) {
            LOGGER.error(e);
            LOGGER.error(e.getStackTrace());
        }
        return result;
    }

    protected String get(String url) {
        LOGGER.info("[GET] URL: {}", url);
        String result = null;
        try {
            Request request = Request.get(url);
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-Type", "application/json");
            request.setHeader("PlayerUUID", this.getIdentifierString());
            Response response = request.execute();
            result = response.returnContent().asString();
        } catch (IOException e) {
            LOGGER.error(e);
            LOGGER.error(e.getStackTrace());
        }
        return result;
    }

    protected String post(String url) {
        return post(url, "");
    }

    protected String post(String url, String body) {
        LOGGER.info("[POST] URL: {}\n{}", url, body);
        HttpPost request = new HttpPost(url);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
        if (this.getIdentifierString() != null && !this.getIdentifierString().isEmpty()) {
            request.setHeader("PlayerUUID", this.getIdentifierString());
        }
        return sendRequest(request);
    }

    protected void put(String url, String body) {
        LOGGER.info("[PUT] URL: {}\n{}", url, body);
        HttpPut request = new HttpPut(url);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
        if (this.getIdentifierString() != null && !this.getIdentifierString().isEmpty()) {
            request.setHeader("PlayerUUID", this.getIdentifierString());
        }
        sendRequest(request);
    }

    private String sendRequest(HttpUriRequest request) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            return httpClient.execute(request, response -> {
                return EntityUtils.toString(response.getEntity());
            });
        } catch (Exception e) {
            throw new CouldNotSendRequestException(e);
        }
    }

}
