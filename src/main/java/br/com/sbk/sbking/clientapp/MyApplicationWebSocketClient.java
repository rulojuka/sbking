package br.com.sbk.sbking.clientapp;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.sbk.sbking.gui.main.ConnectToServer;
import br.com.sbk.sbking.networking.client.SBKingClient;

@Component
public class MyApplicationWebSocketClient {

    private WebSocketClient webSocketClient;
    private WebSocketStompClient webSocketStompClient;
    private MyStompSessionHandler stompSessionHandler;
    private String serverUrl;

    public MyApplicationWebSocketClient(@Autowired ClientComponent clientComponent,
            @Autowired ObjectMapper objectMapper) {
        LOGGER.trace("Initializing MyApplicationWebSocketClient");
        this.webSocketClient = new StandardWebSocketClient();
        this.webSocketStompClient = new WebSocketStompClient(webSocketClient);

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);

        this.webSocketStompClient.setMessageConverter(converter);
        // stompClient.setTaskScheduler(taskScheduler); // for heartbeats

        SBKingClient sbKingClient = clientComponent.getSBKingClient();
        this.stompSessionHandler = new MyStompSessionHandler(sbKingClient);
        String ipAddress = ConnectToServer.getIPAddress();
        this.serverUrl = "ws://" + ipAddress + ":8080/sbking-websocket";
        LOGGER.info("Server websocket address: {}", this.serverUrl);
    }

    public StompSession connect() {
        LOGGER.info("Trying to connect to the websocket!");
        StompSession stompSession = null;
        try {
            stompSession = webSocketStompClient.connect(serverUrl, stompSessionHandler).get();
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return stompSession;
    }

}
