package br.com.sbk.sbking.networking.rest;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.UUID;

import org.apache.http.HttpMessage;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

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

    protected String createAndSendPostRequest(String url, String body) {
        LOGGER.trace("[POST] URL: {}", url);
        LOGGER.trace("Body: {}", body);
        HttpPost httpPost = new HttpPost(url);
        this.fillRequestWithBodyAndJson(httpPost, body);
        return sendRequest(httpPost);
    }

    protected void createAndSendPostRequest(String url) {
        this.createAndSendPostRequest(url, "");
    }

    protected void createAndSendPutRequest(String url, String body) {
        LOGGER.trace("[PUT] URL: {}", url);
        LOGGER.trace("Body: {}", body);
        HttpPut httpPut = new HttpPut(url);
        this.fillRequestWithBodyAndJson(httpPut, body);
        sendRequest(httpPut);
    }

    protected HttpGet createGetRequest(String url) {
        LOGGER.trace("[GET] URL: {}", url);
        HttpGet httpGet = new HttpGet(url);
        this.setJsonAndIdentifierHeaders(httpGet);
        return httpGet;
    }

    private void setJsonAndIdentifierHeaders(HttpRequestBase requestBase) {
        this.setJsonHeaders(requestBase);
        this.setIdentifierHeader(requestBase);
    }

    private void fillRequestWithBodyAndJson(HttpEntityEnclosingRequestBase requestBase, String body) {
        StringEntity requestEntity = new StringEntity(
                body,
                ContentType.APPLICATION_JSON);
        this.setJsonHeaders(requestBase);
        this.setIdentifierHeader(requestBase);
        requestBase.setEntity(requestEntity);
    }

    private void setJsonHeaders(HttpMessage httpMessage) {
        httpMessage.setHeader("Accept", "application/json");
        httpMessage.setHeader("Content-Type", "application/json");
    }

    private void setIdentifierHeader(HttpMessage httpMessage) {
        httpMessage.setHeader("PlayerUUID", this.getIdentifierString());
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

    protected Result sendRequestWithResponse(HttpUriRequest request) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final Result result = httpClient.execute(request, response -> {
                return new Result(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity()));
            });
            return result;
        } catch (Exception e) {
            throw new CouldNotSendRequestException(e);
        }

    }

}
