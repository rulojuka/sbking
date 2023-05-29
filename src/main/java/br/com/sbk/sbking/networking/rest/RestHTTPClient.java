package br.com.sbk.sbking.networking.rest;

import java.util.UUID;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import br.com.sbk.sbking.core.Card;

public class RestHTTPClient {

    private String baseUrl;

    public RestHTTPClient(String ip) {
        this.baseUrl = String.format("http://%s:8080/", ip);
    }

    public void sendHttpPlayCardMessage(Card card, UUID identifier) {
        String url = this.baseUrl + "playcard/";
        final HttpPost httpPost = new HttpPost(url);

        String jsonString = String
                .format("{\"rank\":\"%s\",\"suit\":\"%s\",\"identifier\":\"%s\"}",
                        card.getRank().getSymbol(), card.getSuit().getSymbol(), identifier.toString());

        StringEntity requestEntity = new StringEntity(
                jsonString,
                ContentType.APPLICATION_JSON);

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");

        try {
            httpPost.setEntity(requestEntity);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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
