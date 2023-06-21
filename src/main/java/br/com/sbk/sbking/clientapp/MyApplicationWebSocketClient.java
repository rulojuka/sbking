package br.com.sbk.sbking.clientapp;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Component
public class MyApplicationWebSocketClient {

    private WebSocketClient webSocketClient;
    private WebSocketStompClient webSocketStompClient;
    private MyStompSessionHandler stompSessionHandler;
    private String serverUrl;

    public MyApplicationWebSocketClient() {
        LOGGER.trace("Initializing MyApplicationWebSocketClient");
        this.webSocketClient = new StandardWebSocketClient();
        this.webSocketStompClient = new WebSocketStompClient(webSocketClient);
        this.webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        // stompClient.setTaskScheduler(taskScheduler); // for heartbeats

        this.stompSessionHandler = new MyStompSessionHandler();
        this.serverUrl = "ws://localhost:8080/gs-guide-websocket";
    }

    public StompSession connect() {
        LOGGER.info("Trying to connect!");
        StompSession stompSession = null;
        try {
            stompSession = webSocketStompClient.connect(serverUrl, stompSessionHandler).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stompSession;
    }

}
