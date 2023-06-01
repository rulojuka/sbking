package br.com.sbk.sbking.networking.rest;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.http.client.methods.HttpGet;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.dto.LobbyScreenTableDTO;

public class RestHTTPClient extends BaseRestHTTPClient {

    public RestHTTPClient(String ip, UUID identifier) {
        super(ip, identifier);
    }

    public RestHTTPClient(String ip) {
        this(ip, null);
    }

    public void play(Card card) {
        String url = this.baseUrl + "playcard";
        String body = String.format("{\"rank\":\"%s\",\"suit\":\"%s\"}",
                card.getRank().getSymbol(), card.getSuit().getSymbol());
        createAndSendPostRequest(url, body);
    }

    public void sendCreateTableMessage(String gameName) {
        String url = this.baseUrl + "table";
        String body = String.format("{\"content\":\"%s\"}", gameName);
        createAndSendPostRequest(url, body);
    }

    public void sendJoinTableMessage(UUID tableId) {
        String url = this.baseUrl + "table/join/" + tableId.toString();
        createAndSendPostRequest(url);
    }

    public void leaveTable() {
        String url = this.baseUrl + "table/leave";
        createAndSendPostRequest(url);
    }

    public void moveToSeat(Direction direction) {
        String url = this.baseUrl + "moveToSeat/" + direction.getAbbreviation();
        createAndSendPostRequest(url);
    }

    public void sendNickname(String nickname) {
        String url = this.baseUrl + "player/nickname";
        String body = String.format("{\"content\":\"%s\"}", nickname);
        createAndSendPutRequest(url, body);
    }

    public void claim() {
        String url = this.baseUrl + "claim";
        createAndSendPostRequest(url);
    }

    public void handleClaim(boolean accept) {
        String url = this.baseUrl + "claim/" + accept;
        createAndSendPostRequest(url);
    }

    public void undo() {
        String url = this.baseUrl + "undo";
        createAndSendPostRequest(url);
    }

    public void choosePositive() {
        String url = this.baseUrl + "choosePositiveOrNegative/" + "+";
        createAndSendPostRequest(url);
    }

    public void chooseNegative() {
        String url = this.baseUrl + "choosePositiveOrNegative/" + "-";
        createAndSendPostRequest(url);
    }

    public void chooseGameModeOrStrain(String gameModeOrStrain) {
        String url = this.baseUrl + "chooseGameModeOrStrain";
        String body = String.format("{\"content\":\"%s\"}", gameModeOrStrain);
        createAndSendPostRequest(url, body);
    }

    public List<String> getSpectators() {
        String url = this.baseUrl + "spectators";
        HttpGet httpGet = createGetRequest(url);
        Result result = sendRequestWithResponse(httpGet);
        return Stream
                .of(deserializer.fromJson(result.getContent(), String[].class))
                .collect(Collectors.toList());
    }

    public List<LobbyScreenTableDTO> getTables() {
        String url = this.baseUrl + "tables";
        LOGGER.info("getTables");
        HttpGet httpGet = createGetRequest(url);
        Result result = sendRequestWithResponse(httpGet);
        return Stream
                .of(deserializer.fromJson(result.getContent(), LobbyScreenTableDTO[].class))
                .collect(Collectors.toList());
    }

}
