package br.com.sbk.sbking.clientapp;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompSession;

import br.com.sbk.sbking.networking.websockets.HelloMessage;

// @Component // Uncomment for spring to run this
public class ChatComponent {

    private StompSession session;

    public ChatComponent(@Autowired MyApplicationWebSocketClient myApplicationWebSocketClient) {
        LOGGER.trace("Initializing ChatComponent");
        session = myApplicationWebSocketClient.connect();
    }

    public void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        for (;;) {
            System.out.print(" >> ");
            System.out.flush();
            String line = null;
            try {
                line = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line == null) {
                break;
            }
            if (line.length() == 0) {
                continue;
            }
            String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
            HelloMessage helloMessage = new HelloMessage(time + ": " + line);
            session.send("/app/hello", helloMessage);
        }
    }

}
