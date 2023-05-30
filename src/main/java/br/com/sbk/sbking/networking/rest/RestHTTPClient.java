package br.com.sbk.sbking.networking.rest;

import java.util.UUID;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;

public class RestHTTPClient extends BaseRestHTTPClient {

    public RestHTTPClient(String ip, UUID identifier) {
        super(ip, identifier);
    }

    public RestHTTPClient(String ip) {
        this(ip, null);
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
        String body = this.simpleBodyWithIdentifier();
        createAndSendPostRequest(url, body);
    }

    public void leaveTable() {
        String url = this.baseUrl + "table/leave";
        String body = this.simpleBodyWithIdentifier();
        createAndSendPostRequest(url, body);
    }

    public void moveToSeat(Direction direction) {
        String url = this.baseUrl + "moveToSeat/" + direction.getAbbreviation();
        String body = this.simpleBodyWithIdentifier();
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
        String body = this.simpleBodyWithIdentifier();
        createAndSendPostRequest(url, body);
    }

    public void handleClaim(boolean accept) {
        String url = this.baseUrl + "claim/" + accept;
        String body = this.simpleBodyWithIdentifier();
        createAndSendPostRequest(url, body);
    }

    public void undo() {
        String url = this.baseUrl + "undo";
        String body = this.simpleBodyWithIdentifier();
        createAndSendPostRequest(url, body);
    }

    public void choosePositive() {
        String url = this.baseUrl + "choosePositiveOrNegative/" + "+";
        String body = this.simpleBodyWithIdentifier();
        createAndSendPostRequest(url, body);
    }

    public void chooseNegative() {
        String url = this.baseUrl + "choosePositiveOrNegative/" + "-";
        String body = this.simpleBodyWithIdentifier();
        createAndSendPostRequest(url, body);
    }

}
