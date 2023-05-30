package br.com.sbk.sbking.networking.rest;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.UUID;

import org.apache.http.HttpMessage;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;

public class RestHTTPClient {

    private String baseUrl;
    private UUID identifier;

    public RestHTTPClient(String ip, UUID identifier) {
        this.baseUrl = String.format("http://%s:8080/", ip);
        this.identifier = identifier;
    }

    public RestHTTPClient(String ip) {
        this(ip, null);
    }

    public void setIdentifier(String identifier) {
        this.identifier = UUID.fromString(identifier);
    }

    public String getIdentifierString() {
        if (this.identifier == null) {
            throw new IdentifierNotSetException();
        }
        return identifier.toString();
    }

    public void play(Card card) {
        String url = this.baseUrl + "playcard";

        String body = String
                .format("{\"rank\":\"%s\",\"suit\":\"%s\",\"identifier\":\"%s\"}",
                        card.getRank().getSymbol(), card.getSuit().getSymbol(), this.getIdentifierString());

        createAndSendPostRequest(url, body);
    }

    public void sendCreateTableMessage(String gameName) {
        String url = this.baseUrl + "table";
        String body = String
                .format("{\"content\":\"%s\",\"identifier\":\"%s\"}",
                        gameName, this.getIdentifierString());
        createAndSendPostRequest(url, body);
    }

    public void sendJoinTableMessage(UUID tableId) {
        String url = this.baseUrl + "table/join/" + tableId.toString();
        String body = String
                .format("{\"identifier\":\"%s\"}", this.getIdentifierString());
        createAndSendPostRequest(url, body);
    }

    public void leaveTable() {
        String url = this.baseUrl + "table/leave";
        String body = String
                .format("{\"identifier\":\"%s\"}", this.getIdentifierString());
        createAndSendPostRequest(url, body);
    }

    public void moveToSeat(Direction direction) {
        String url = this.baseUrl + "moveToSeat/" + direction.getAbbreviation();
        String body = String
                .format("{\"identifier\":\"%s\"}", this.getIdentifierString());
        createAndSendPostRequest(url, body);
    }

    public void sendNickname(String nickname) {
        String url = this.baseUrl + "player/nickname";
        String body = String
                .format("{\"content\":\"%s\",\"identifier\":\"%s\"}", nickname, this.getIdentifierString());
        createAndSendPutRequest(url, body);
    }

    public void claim() {
        String url = this.baseUrl + "claim";
        String body = String
                .format("{\"identifier\":\"%s\"}", this.getIdentifierString());
        createAndSendPostRequest(url, body);
    }

    public void handleClaim(boolean accept) {
        String url = this.baseUrl + "claim/" + accept;
        String body = String
                .format("{\"identifier\":\"%s\"}", this.getIdentifierString());
        createAndSendPostRequest(url, body);
    }

    public void undo() {
        String url = this.baseUrl + "undo";
        String body = String
                .format("{\"identifier\":\"%s\"}", this.getIdentifierString());
        createAndSendPostRequest(url, body);
    }

    private void createAndSendPostRequest(String url, String body) {
        LOGGER.info("[POST] URL: " + url);
        LOGGER.info("Body: " + body);
        HttpPost httpPost = new HttpPost(url);
        this.fillRequestWithBodyAndJson(httpPost, body);
        sendRequest(httpPost);
    }

    private void createAndSendPutRequest(String url, String body) {
        LOGGER.info("[PUT] URL: " + url);
        LOGGER.info("Body: " + body);
        HttpPut httpPut = new HttpPut(url);
        this.fillRequestWithBodyAndJson(httpPut, body);
        sendRequest(httpPut);
    }

    private void fillRequestWithBodyAndJson(HttpEntityEnclosingRequestBase requestBase, String body) {
        StringEntity requestEntity = new StringEntity(
                body,
                ContentType.APPLICATION_JSON);
        this.setJsonHeaders(requestBase);
        requestBase.setEntity(requestEntity);
    }

    private void setJsonHeaders(HttpMessage httpMessage) {
        httpMessage.setHeader("Accept", "application/json");
        httpMessage.setHeader("Content-Type", "application/json");
    }

    private void sendRequest(HttpUriRequest request) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            httpClient.execute(request, response -> {
                return response;
            });
        } catch (Exception e) {
            throw new CouldNotSendRequestException(e);
        }
    }

}
