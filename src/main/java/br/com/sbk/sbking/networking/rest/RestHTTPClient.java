package br.com.sbk.sbking.networking.rest;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.UUID;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import br.com.sbk.sbking.core.Card;

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

    public void sendHttpPlayCardMessage(Card card) {
        String url = this.baseUrl + "playcard/";

        String body = String
                .format("{\"rank\":\"%s\",\"suit\":\"%s\",\"identifier\":\"%s\"}",
                        card.getRank().getSymbol(), card.getSuit().getSymbol(), identifier.toString());

        createAndSendPostRequest(url, body);
    }

    public void sendCreateTableMessage(String gameName) {
        String url = this.baseUrl + "table/";
        String body = String
                .format("{\"content\":\"%s\",\"identifier\":\"%s\"}",
                        gameName, identifier.toString());
        createAndSendPostRequest(url, body);
    }

    private void createAndSendPostRequest(String url, String body) {
        LOGGER.info("URL: " + url);
        LOGGER.info("Body: " + body);
        if (this.identifier == null) {
            throw new IdentifierNotSetException();
        }
        sendPostRequest(createPostRequest(url, body));
    }

    private HttpPost createPostRequest(String url, String body) {
        final HttpPost httpPost = new HttpPost(url);

        StringEntity requestEntity = new StringEntity(
                body,
                ContentType.APPLICATION_JSON);

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");

        try {
            httpPost.setEntity(requestEntity);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return httpPost;
    }

    private void sendPostRequest(HttpPost httpPost) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            httpClient.execute(httpPost, response -> {
                return response;
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
